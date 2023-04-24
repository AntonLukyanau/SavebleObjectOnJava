package org.example;

import java.lang.reflect.Field;

public class GeneralFieldToYamlConverter implements FieldToYamlConverter {

    public String toYAMLLine(Object source, Field field) {
        if (canConvert(field)) {
            try {
                field.setAccessible(true);
                Object value = field.get(source);
                return field.getName() + ": " + value;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        throw new IllegalArgumentException("I can't convert this field");
    }

    public boolean canConvert(Field field) {
        Class<?> type = field.getType();
        return type.isPrimitive()
                || type == String.class
                || type == Boolean.class
                || type == Character.class
                || type.getSuperclass() == Number.class;
    }

}
