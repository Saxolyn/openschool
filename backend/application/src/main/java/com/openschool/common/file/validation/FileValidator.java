package com.openschool.common.file.validation;

import com.openschool.common.file.command.UploadFileCommand;
import com.openschool.common.file.exception.FileValidationException;
import com.openschool.domain.common.file.FileCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Pure Java validation logic for file operations
 */
public class FileValidator {
    
    // File size limits (in bytes)
    private static final Map<FileCategory, Long> MAX_FILE_SIZES = Map.of(
        FileCategory.IMAGE, 5L * 1024 * 1024,        // 5MB
        FileCategory.DOCUMENT, 10L * 1024 * 1024,    // 10MB
        FileCategory.VIDEO, 100L * 1024 * 1024,      // 100MB
        FileCategory.AUDIO, 50L * 1024 * 1024,       // 50MB
        FileCategory.SPREADSHEET, 10L * 1024 * 1024, // 10MB
        FileCategory.PRESENTATION, 20L * 1024 * 1024, // 20MB
        FileCategory.ARCHIVE, 50L * 1024 * 1024      // 50MB
    );
    
    // Allowed file types
    private static final Map<FileCategory, String[]> ALLOWED_TYPES = Map.of(
        FileCategory.IMAGE, new String[]{"image/jpeg", "image/png", "image/gif", "image/webp"},
        FileCategory.DOCUMENT, new String[]{"application/pdf", "application/msword", 
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
        FileCategory.VIDEO, new String[]{"video/mp4", "video/avi", "video/mov", "video/wmv"},
        FileCategory.AUDIO, new String[]{"audio/mp3", "audio/wav", "audio/ogg", "audio/m4a"},
        FileCategory.SPREADSHEET, new String[]{"application/vnd.ms-excel", 
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
        FileCategory.PRESENTATION, new String[]{"application/vnd.ms-powerpoint",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
        FileCategory.ARCHIVE, new String[]{"application/zip", "application/x-rar-compressed", 
            "application/x-7z-compressed"}
    );
    
    // Dangerous file extensions
    private static final String[] DANGEROUS_EXTENSIONS = {
        ".exe", ".bat", ".cmd", ".com", ".pif", ".scr", ".vbs", ".js", ".jar", ".sh"
    };
    
    public static void validateUploadCommand(UploadFileCommand command) {
        List<String> errors = new ArrayList<>();
        
        // Validate file data
        if (command.getFileData() == null || command.getFileData().length == 0) {
            errors.add("File data is required");
        }
        
        // Validate file name
        if (command.getOriginalFileName() == null || command.getOriginalFileName().trim().isEmpty()) {
            errors.add("File name is required");
        } else {
            validateFileName(command.getOriginalFileName(), errors);
        }
        
        // Validate content type
        if (command.getContentType() == null || command.getContentType().trim().isEmpty()) {
            errors.add("Content type is required");
        }
        
        // Validate file size
        if (command.getFileData() != null) {
            validateFileSize(command.getFileData().length, command.getCategory(), errors);
        }
        
        // Validate file type
        if (command.isValidateFileType() && command.getContentType() != null) {
            validateFileType(command.getContentType(), command.getCategory(), errors);
        }
        
        // Validate uploader
        if (command.getUploadedBy() == null) {
            errors.add("Uploader ID is required");
        }
        
        if (!errors.isEmpty()) {
            throw new FileValidationException("File validation failed", errors);
        }
    }
    
    private static void validateFileName(String fileName, List<String> errors) {
        // Check for dangerous extensions
        String lowerFileName = fileName.toLowerCase();
        for (String ext : DANGEROUS_EXTENSIONS) {
            if (lowerFileName.endsWith(ext)) {
                errors.add("File type not allowed: " + ext);
                break;
            }
        }
        
        // Check for invalid characters
        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
            errors.add("File name contains invalid characters");
        }
        
        // Check file name length
        if (fileName.length() > 255) {
            errors.add("File name is too long (max 255 characters)");
        }
    }
    
    private static void validateFileSize(long fileSize, FileCategory category, List<String> errors) {
        Long maxSize = MAX_FILE_SIZES.get(category);
        if (maxSize == null) {
            maxSize = 10L * 1024 * 1024; // Default 10MB
        }
        
        if (fileSize > maxSize) {
            errors.add(String.format("File size exceeds maximum allowed size of %s", 
                formatFileSize(maxSize)));
        }
        
        if (fileSize == 0) {
            errors.add("File is empty");
        }
    }
    
    private static void validateFileType(String contentType, FileCategory category, List<String> errors) {
        String[] allowedTypes = ALLOWED_TYPES.get(category);
        if (allowedTypes == null) {
            return; // No restrictions for this category
        }
        
        boolean isAllowed = Arrays.asList(allowedTypes).contains(contentType);
        if (!isAllowed) {
            errors.add(String.format("File type '%s' is not allowed for category '%s'", 
                contentType, category.getDisplayName()));
        }
    }
    
    public static long getMaxFileSize(FileCategory category) {
        return MAX_FILE_SIZES.getOrDefault(category, 10L * 1024 * 1024);
    }
    
    public static String[] getAllowedFileTypes(FileCategory category) {
        return ALLOWED_TYPES.get(category);
    }
    
    private static String formatFileSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.1f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        } else {
            return String.format("%.1f GB", bytes / (1024.0 * 1024.0 * 1024.0));
        }
    }
}
