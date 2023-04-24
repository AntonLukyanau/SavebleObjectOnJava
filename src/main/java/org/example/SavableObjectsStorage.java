package org.example;

import java.io.File;
import java.util.logging.Logger;

public class SavableObjectsStorage {
    private static final Logger log = Logger.getLogger(SavableObjectsStorage.class.getName());

    private final SavableObjectWriter writer;
    private final SavableObjectReader reader;
    private final IdHolder idHolder;

    public SavableObjectsStorage() {
        this(new YAMLSavableObjectReader(), new YAMLSavableObjectWriter(), new CommonIdHolder());
        File folder = new File(Config.FOLDER_TO_SAVE);
        if (folder.mkdir()) {
            log.info("Folder: " + Config.FOLDER_TO_SAVE + " was created");
        }
    }

    private SavableObjectsStorage(SavableObjectReader reader, SavableObjectWriter writer, IdHolder idHolder) {
        this.reader = reader;
        this.writer = writer;
        this.idHolder = idHolder;
    }

    public void save(SavableObject savableObject) {
        Long objId = idHolder.resolveId(savableObject);
        savableObject.setId(objId);
        String path = Config.FOLDER_TO_SAVE + File.separator + objId + Config.EXTENSION;
        writer.saveTo(path, savableObject);
    }

    public void delete(SavableObject savableObject) {
        if (savableObject.getId() != null) {
            File entry = new File(Config.FOLDER_TO_SAVE + File.separator + savableObject.getId() + Config.EXTENSION);
            if (entry.delete()) {
                savableObject.setId(null);
            } else {
                log.warning("entity was not deleted!");
            }
        } else {
            log.warning("detected attempt to delete unsaved object");
        }
    }

    public SavableObject findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must be non null");
        }
        File entry = new File(Config.FOLDER_TO_SAVE + File.separator + id + Config.EXTENSION);
        if (entry.exists()) {
            return (SavableObject) reader.readFrom(Config.FOLDER_TO_SAVE + File.separator + id + Config.EXTENSION);
        }
        return null;
    }
}
