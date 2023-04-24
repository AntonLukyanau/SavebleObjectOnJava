package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class YAMLSavableObjectWriter implements SavableObjectWriter {

    private final Set<FieldToYamlConverter> converters;
    private final StringUtil stringUtil = new StringUtil();

    public YAMLSavableObjectWriter() {
        converters = new HashSet<>();
        converters.add(new GeneralFieldToYamlConverter());
    }

    public void saveTo(String pathToSave, Object obj) {
        List<String> lines = new ArrayList<>();
        Class<?> aClass = obj.getClass();
        lines.add(aClass.getName() + ":");
        lines.addAll(createLines(obj, 0));
        try (FileWriter writer = new FileWriter(pathToSave)) {
            for (int i = 0; i < lines.size() - 1; i++) {
                writer.write(lines.get(i));
                writer.write("\n");
            }
            writer.write(lines.get(lines.size() - 1));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private List<String> createLines(Object obj, int indentCount) {
        try {
            List<String> lines = new ArrayList<>();
            Class<?> aClass = obj.getClass();
            int fieldIndent = indentCount + 1;
            tryAddLineWithId(obj, lines, fieldIndent);
            Field[] fields = aClass.getDeclaredFields();
            for (Field field : fields) {
                FieldToYamlConverter converter = resolveConverterFor(field);
                if (converter != null) {
                    String line = stringUtil.addIndents(converter.toYAMLLine(obj, field), fieldIndent);
                    lines.add(line);
                } else {
                    field.setAccessible(true);
                    Object objFieldValue = field.get(obj);
                    lines.add(stringUtil.addIndents(field.getName() + ":", fieldIndent));
                    List<String> restLines = createLines(objFieldValue, fieldIndent);
                    lines.addAll(restLines);
                }
            }
            return lines;
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void tryAddLineWithId(Object obj, List<String> lines, int fieldIndent)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class<?> superclass = obj.getClass().getSuperclass();
        if (superclass == SavableObject.class) {
            Method getIdMethod = superclass.getMethod("getId");
            Long id = (Long) getIdMethod.invoke(obj);
            lines.add(stringUtil.addIndents("id: " + id, fieldIndent));
        }
    }

    private FieldToYamlConverter resolveConverterFor(Field field) {
        for (FieldToYamlConverter converter : converters) {
            if (converter.canConvert(field)) {
                return converter;
            }
        }
        return null;
    }
}
