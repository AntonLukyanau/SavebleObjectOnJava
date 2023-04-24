package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CommonIdHolder implements IdHolder {

    public Long resolveId(SavableObject obj) {
        if (obj.getId() != null) {
            return obj.getId();
        }
        File entitySeqContainer = new File(Config.PATH_TO_SEQ);
        if (entitySeqContainer.exists()) {
            long lastId;
            try (BufferedReader reader = new BufferedReader(new FileReader(Config.PATH_TO_SEQ))) {
                lastId = Long.parseLong(reader.readLine());
            } catch (IOException e) {
                throw new RuntimeException(e); /* todo create new own runtime exception */
            }
            Long newId = lastId + Config.SEQ_STEP;
            setSeqValue(newId);
            return newId;
        }
        setSeqValue(1L);
        return 1L;

    }

    private void setSeqValue(Long id) {
        try (FileWriter out = new FileWriter(Config.PATH_TO_SEQ)) {
            out.write(id.toString());
        } catch (IOException e) {
            throw new RuntimeException(e); /* todo create new own runtime exception */
        }
    }

}
