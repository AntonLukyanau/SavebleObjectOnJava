package org.example;

import java.lang.reflect.Field;

public interface FieldToYamlConverter {

    String toYAMLLine(Object source, Field field);

    boolean canConvert(Field field);

}
