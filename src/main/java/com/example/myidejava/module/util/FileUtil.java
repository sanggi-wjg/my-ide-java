package com.example.myidejava.module.util;

import com.example.myidejava.core.exception.error.UtilException;
import com.example.myidejava.core.exception.error.code.ErrorCode;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileUtil {

    private FileUtil() {
    }

    public static File createTemporaryFile(String extension, String content) {
        String filename = generateFileNameWithUUID(extension);
        File file = new File(filename);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        } catch (IOException e) {
            throw new UtilException(ErrorCode.FAIL_TO_CREATE_FILE);
        }
        return file;
    }

    public static String generateFileNameWithUUID(String extension) {
        return UUID.randomUUID() + "." + extension;
    }

    public static void unlink(File file) {
        if (file.exists()) {
            file.delete();
        }
    }

}
