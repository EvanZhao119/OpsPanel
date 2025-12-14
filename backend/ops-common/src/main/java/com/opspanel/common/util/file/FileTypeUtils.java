package com.opspanel.common.util.file;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for detecting file types.
 */
public class FileTypeUtils {

    /**
     * Check if the file is an image based on extension.
     */
    public static boolean isImage(String fileName) {
        String ext = getFileExtension(fileName);
        return switch (ext) {
            case "jpg", "jpeg", "png", "gif", "bmp", "webp" -> true;
            default -> false;
        };
    }

    /**
     * Check if the file is a document.
     */
    public static boolean isDocument(String fileName) {
        String ext = getFileExtension(fileName);
        return switch (ext) {
            case "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt" -> true;
            default -> false;
        };
    }

    /**
     * Check if the file is a video.
     */
    public static boolean isVideo(String fileName) {
        String ext = getFileExtension(fileName);
        return switch (ext) {
            case "mp4", "avi", "mov", "mkv", "flv" -> true;
            default -> false;
        };
    }

    /**
     * Get the MIME type of a file.
     */
    public static String getMimeType(String filePath) {
        try {
            Path path = Paths.get(filePath);
            return Files.probeContentType(path);
        } catch (Exception e) {
            return "application/octet-stream";
        }
    }

    /**
     * Extract the file extension from a filename.
     */
    public static String getFileExtension(String fileName) {
        if (fileName == null) return "";
        int index = fileName.lastIndexOf('.');
        return index != -1 ? fileName.substring(index + 1).toLowerCase() : "";
    }
}
