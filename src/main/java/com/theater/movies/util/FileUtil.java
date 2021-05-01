package com.theater.movies.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@UtilityClass
@Slf4j
public class FileUtil {

    private final static String UPLOAD_FOLDER = "C:\\Uploads\\";

    public static String saveFile(MultipartFile file) {
        if (!file.isEmpty()) {
            var filename = generateRandomFilename(file.getOriginalFilename());
            Path path = Paths.get(UPLOAD_FOLDER + filename);
            try {
                Files.write(path, file.getBytes());
            } catch (IOException e) {
                log.info("File uploaded is empty!");
            }
            return filename;
        }
        return null;
    }

    private static String generateRandomFilename(String filename) {
        return UUID.randomUUID() + "." + Optional.of(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1)).get();
    }
}
