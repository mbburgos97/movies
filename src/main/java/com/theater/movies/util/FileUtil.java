package com.theater.movies.util;

import com.theater.movies.enums.FileType;
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

    public final static String UPLOAD_FOLDER = "C:\\Uploads\\";

    public static String getFilePath(String filename, FileType fileType) {
        switch (fileType) {
            case VIDEO: return "/video/" + filename;
            case IMAGE: return "/image/" + filename;
        }
        return null;
    }

    public static String saveFile(MultipartFile file) {
        if (!file.isEmpty()) {
            var filename = generateRandomFilename(file.getOriginalFilename());
            Path path = Paths.get(UPLOAD_FOLDER + filename);
            try {
                Files.write(path, file.getBytes());
            } catch (IOException e) {
                log.info("Error saving file {}", file.getOriginalFilename(), e);
            }
            return filename;
        }
        return null;
    }

    public static byte[] getFile(String filename) {
        Path path = Paths.get(UPLOAD_FOLDER + filename);

        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            log.error("Error found when reading {}", filename, e);
        }
        return null;
    }

    private static String generateRandomFilename(String filename) {
        return UUID.randomUUID() + "." + Optional.of(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1)).get();
    }
}
