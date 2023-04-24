package org.example;

import java.io.File;

public class Config {

    public static final String FOLDER_TO_SAVE = SavableObject.class.getSimpleName();
    public static final String EXTENSION = ".yaml";
    public static final int INDENT = 2;

    public static final String PATH_TO_SEQ = FOLDER_TO_SAVE + File.separator + "saved_object.seq";
    public static final int SEQ_STEP = 1;

}
