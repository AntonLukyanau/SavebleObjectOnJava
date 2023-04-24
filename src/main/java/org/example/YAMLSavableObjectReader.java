package org.example;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class YAMLSavableObjectReader implements SavableObjectReader {

    private final StringUtil stringUtil = new StringUtil();

    public Object readFrom(String path) {
        try (Stream<String> linesStream = Files.lines(Paths.get(path))) {
            String[] strings = linesStream
                    .filter(line -> !line.isBlank())
                    .toArray(String[]::new);
            return readObject(strings);
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalArgumentException("Incorrect file!", e);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new IllegalStateException("Savable object must has public constructor without parameters", e);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private Object readObject(String[] yamlLines) throws ReflectiveOperationException {
        Class<?> aClass = Class.forName(stringUtil.trimLastSymbol(yamlLines[0]).trim());
        Object instance = aClass.getConstructor().newInstance();
        String id = yamlLines[1].split(":")[1].trim();
        trySetId(id, aClass, instance);
        Map<String, Field> fields = fieldNamesToFields(aClass);
        readFields(yamlLines, instance, fields);
        return instance;
    }

    private static void trySetId(String value, Class<?> aClass, Object instance) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (aClass.getSuperclass() == SavableObject.class) {
            Method setIdMethod = aClass.getSuperclass().getDeclaredMethod("setId", Long.class);
            setIdMethod.setAccessible(true);
            setIdMethod.invoke(instance, Long.parseLong(value));
        }
    }

    private static Map<String, Field> fieldNamesToFields(Class<?> aClass) {
        return Arrays.stream(aClass.getDeclaredFields())
                .collect(Collectors.toMap(Field::getName, field -> field));
    }

    private void readFields(String[] strings, Object instance, Map<String, Field> fields) throws ReflectiveOperationException {
        for (int curLinePos = 1; curLinePos < strings.length; curLinePos++) {
            String[] entry = strings[curLinePos].split(":");
            String fieldName = entry[0].trim();
            Field field = fields.get(fieldName);
            if (entry.length == 1 && field != null) {
                String[] typeWithRest = concatLinesToTypeInfo(strings, curLinePos, field.getType());
                Object value = readObject(typeWithRest);
                field.setAccessible(true);
                field.set(instance, value);
            } else if (field != null) {
                field.setAccessible(true);
                field.set(instance, entry[1].trim());
            }
        }
    }

    private String[] concatLinesToTypeInfo(String[] lines, int currentPosition, Class<?> fieldType) {
        int indentForType = stringUtil.indentsBeforeText(lines[0]);
        String type = stringUtil.addIndents(fieldType.getName() + ":", indentForType + 1);
        String[] rest = Arrays.copyOfRange(lines, currentPosition + 1, lines.length);
        String[] typeWithRest = new String[1 + rest.length];
        typeWithRest[0] = type;
        System.arraycopy(rest, 0, typeWithRest, 1, rest.length);
        return typeWithRest;
    }

}
