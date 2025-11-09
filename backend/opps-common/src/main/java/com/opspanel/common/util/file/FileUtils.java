package com.opspanel.common.util.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;

/**
 * Utility class for common file operations.
 */
public class FileUtils {

    /**
     * Delete a file from the filesystem.
     * @param filePath absolute or relative path to the file
     * @return true if the file was deleted successfully
     */
    public static boolean deleteFile(String filePath) {
        try {
            return Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Copy a file to a new location.
     */
    public static void copyFile(String sourcePath, String targetPath) throws IOException {
        Files.copy(Paths.get(sourcePath), Paths.get(targetPath), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Write text content to a file (creates or overwrites).
     */
    public static void writeStringToFile(String path, String content) throws IOException {
        Files.writeString(Paths.get(path), content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * Read the contents of a text file.
     */
    public static String readFileToString(String path) throws IOException {
        return Files.readString(Paths.get(path));
    }

    public static OutputStream newOutputStream(String filePath) throws IOException {
        Path path = Path.of(filePath);
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        return Files.newOutputStream(path);
    }

    public static InputStream newInputStream(String filePath) throws IOException {
        Path path = Path.of(filePath);
        if (!Files.exists(path)) {
            throw new FileNotFoundException("File not found: " + filePath);
        }
        return Files.newInputStream(path);
    }

}
