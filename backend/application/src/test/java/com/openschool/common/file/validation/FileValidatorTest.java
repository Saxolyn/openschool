package com.openschool.common.file.validation;

import com.openschool.common.file.command.UploadFileCommand;
import com.openschool.common.file.exception.FileValidationException;
import com.openschool.domain.common.file.FileCategory;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FileValidatorTest {

    @Test
    void validateUploadCommand_ShouldNotThrowException_WhenValidCommand() {
        // Given
        byte[] fileData = "test file content".getBytes();
        UploadFileCommand command = UploadFileCommand.builder()
                .fileData(fileData)
                .originalFileName("test.pdf")
                .contentType("application/pdf")
                .category(FileCategory.DOCUMENT)
                .uploadedBy(UUID.randomUUID())
                .validateFileType(true)
                .build();

        // When & Then
        assertDoesNotThrow(() -> FileValidator.validateUploadCommand(command));
    }

    @Test
    void validateUploadCommand_ShouldThrowException_WhenFileDataIsNull() {
        // Given
        UploadFileCommand command = UploadFileCommand.builder()
                .fileData(null)
                .originalFileName("test.pdf")
                .contentType("application/pdf")
                .category(FileCategory.DOCUMENT)
                .uploadedBy(UUID.randomUUID())
                .build();

        // When & Then
        FileValidationException exception = assertThrows(FileValidationException.class,
                () -> FileValidator.validateUploadCommand(command));
        
        assertTrue(exception.getValidationErrors().contains("File data is required"));
    }

    @Test
    void validateUploadCommand_ShouldThrowException_WhenFileDataIsEmpty() {
        // Given
        UploadFileCommand command = UploadFileCommand.builder()
                .fileData(new byte[0])
                .originalFileName("test.pdf")
                .contentType("application/pdf")
                .category(FileCategory.DOCUMENT)
                .uploadedBy(UUID.randomUUID())
                .build();

        // When & Then
        FileValidationException exception = assertThrows(FileValidationException.class,
                () -> FileValidator.validateUploadCommand(command));
        
        assertTrue(exception.getValidationErrors().contains("File data is required"));
    }

    @Test
    void validateUploadCommand_ShouldThrowException_WhenFileNameIsNull() {
        // Given
        byte[] fileData = "test file content".getBytes();
        UploadFileCommand command = UploadFileCommand.builder()
                .fileData(fileData)
                .originalFileName(null)
                .contentType("application/pdf")
                .category(FileCategory.DOCUMENT)
                .uploadedBy(UUID.randomUUID())
                .build();

        // When & Then
        FileValidationException exception = assertThrows(FileValidationException.class,
                () -> FileValidator.validateUploadCommand(command));
        
        assertTrue(exception.getValidationErrors().contains("File name is required"));
    }

    @Test
    void validateUploadCommand_ShouldThrowException_WhenFileNameIsEmpty() {
        // Given
        byte[] fileData = "test file content".getBytes();
        UploadFileCommand command = UploadFileCommand.builder()
                .fileData(fileData)
                .originalFileName("")
                .contentType("application/pdf")
                .category(FileCategory.DOCUMENT)
                .uploadedBy(UUID.randomUUID())
                .build();

        // When & Then
        FileValidationException exception = assertThrows(FileValidationException.class,
                () -> FileValidator.validateUploadCommand(command));
        
        assertTrue(exception.getValidationErrors().contains("File name is required"));
    }

    @Test
    void validateUploadCommand_ShouldThrowException_WhenContentTypeIsNull() {
        // Given
        byte[] fileData = "test file content".getBytes();
        UploadFileCommand command = UploadFileCommand.builder()
                .fileData(fileData)
                .originalFileName("test.pdf")
                .contentType(null)
                .category(FileCategory.DOCUMENT)
                .uploadedBy(UUID.randomUUID())
                .build();

        // When & Then
        FileValidationException exception = assertThrows(FileValidationException.class,
                () -> FileValidator.validateUploadCommand(command));
        
        assertTrue(exception.getValidationErrors().contains("Content type is required"));
    }

    @Test
    void validateUploadCommand_ShouldThrowException_WhenUploaderIsNull() {
        // Given
        byte[] fileData = "test file content".getBytes();
        UploadFileCommand command = UploadFileCommand.builder()
                .fileData(fileData)
                .originalFileName("test.pdf")
                .contentType("application/pdf")
                .category(FileCategory.DOCUMENT)
                .uploadedBy(null)
                .build();

        // When & Then
        FileValidationException exception = assertThrows(FileValidationException.class,
                () -> FileValidator.validateUploadCommand(command));
        
        assertTrue(exception.getValidationErrors().contains("Uploader ID is required"));
    }

    @Test
    void validateUploadCommand_ShouldThrowException_WhenDangerousFileExtension() {
        // Given
        byte[] fileData = "malicious content".getBytes();
        UploadFileCommand command = UploadFileCommand.builder()
                .fileData(fileData)
                .originalFileName("virus.exe")
                .contentType("application/octet-stream")
                .category(FileCategory.OTHER)
                .uploadedBy(UUID.randomUUID())
                .build();

        // When & Then
        FileValidationException exception = assertThrows(FileValidationException.class,
                () -> FileValidator.validateUploadCommand(command));
        
        assertTrue(exception.getValidationErrors().stream()
                .anyMatch(error -> error.contains("File type not allowed: .exe")));
    }

    @Test
    void validateUploadCommand_ShouldThrowException_WhenFileNameContainsInvalidCharacters() {
        // Given
        byte[] fileData = "test content".getBytes();
        UploadFileCommand command = UploadFileCommand.builder()
                .fileData(fileData)
                .originalFileName("../../../etc/passwd")
                .contentType("text/plain")
                .category(FileCategory.OTHER)
                .uploadedBy(UUID.randomUUID())
                .build();

        // When & Then
        FileValidationException exception = assertThrows(FileValidationException.class,
                () -> FileValidator.validateUploadCommand(command));
        
        assertTrue(exception.getValidationErrors().stream()
                .anyMatch(error -> error.contains("File name contains invalid characters")));
    }

    @Test
    void validateUploadCommand_ShouldThrowException_WhenFileSizeExceedsLimit() {
        // Given
        byte[] fileData = new byte[6 * 1024 * 1024]; // 6MB for image (limit is 5MB)
        UploadFileCommand command = UploadFileCommand.builder()
                .fileData(fileData)
                .originalFileName("large_image.jpg")
                .contentType("image/jpeg")
                .category(FileCategory.IMAGE)
                .uploadedBy(UUID.randomUUID())
                .validateFileType(true)
                .build();

        // When & Then
        FileValidationException exception = assertThrows(FileValidationException.class,
                () -> FileValidator.validateUploadCommand(command));
        
        assertTrue(exception.getValidationErrors().stream()
                .anyMatch(error -> error.contains("File size exceeds maximum allowed size")));
    }

    @Test
    void validateUploadCommand_ShouldThrowException_WhenFileTypeNotAllowedForCategory() {
        // Given
        byte[] fileData = "test content".getBytes();
        UploadFileCommand command = UploadFileCommand.builder()
                .fileData(fileData)
                .originalFileName("test.txt")
                .contentType("text/plain")
                .category(FileCategory.IMAGE)
                .uploadedBy(UUID.randomUUID())
                .validateFileType(true)
                .build();

        // When & Then
        FileValidationException exception = assertThrows(FileValidationException.class,
                () -> FileValidator.validateUploadCommand(command));
        
        assertTrue(exception.getValidationErrors().stream()
                .anyMatch(error -> error.contains("File type 'text/plain' is not allowed for category 'Image'")));
    }

    @Test
    void validateUploadCommand_ShouldNotThrowException_WhenFileTypeValidationDisabled() {
        // Given
        byte[] fileData = "test content".getBytes();
        UploadFileCommand command = UploadFileCommand.builder()
                .fileData(fileData)
                .originalFileName("test.txt")
                .contentType("text/plain")
                .category(FileCategory.IMAGE)
                .uploadedBy(UUID.randomUUID())
                .validateFileType(false)
                .build();

        // When & Then
        assertDoesNotThrow(() -> FileValidator.validateUploadCommand(command));
    }

    @Test
    void getMaxFileSize_ShouldReturnCorrectSize_ForImageCategory() {
        // When
        long maxSize = FileValidator.getMaxFileSize(FileCategory.IMAGE);

        // Then
        assertEquals(5L * 1024 * 1024, maxSize); // 5MB
    }

    @Test
    void getMaxFileSize_ShouldReturnCorrectSize_ForDocumentCategory() {
        // When
        long maxSize = FileValidator.getMaxFileSize(FileCategory.DOCUMENT);

        // Then
        assertEquals(10L * 1024 * 1024, maxSize); // 10MB
    }

    @Test
    void getMaxFileSize_ShouldReturnDefaultSize_ForUnknownCategory() {
        // When
        long maxSize = FileValidator.getMaxFileSize(FileCategory.OTHER);

        // Then
        assertEquals(10L * 1024 * 1024, maxSize); // Default 10MB
    }

    @Test
    void getAllowedFileTypes_ShouldReturnCorrectTypes_ForImageCategory() {
        // When
        String[] allowedTypes = FileValidator.getAllowedFileTypes(FileCategory.IMAGE);

        // Then
        assertNotNull(allowedTypes);
        assertTrue(java.util.Arrays.asList(allowedTypes).contains("image/jpeg"));
        assertTrue(java.util.Arrays.asList(allowedTypes).contains("image/png"));
    }

    @Test
    void getAllowedFileTypes_ShouldReturnNull_ForCategoryWithoutRestrictions() {
        // When
        String[] allowedTypes = FileValidator.getAllowedFileTypes(FileCategory.OTHER);

        // Then
        assertNull(allowedTypes);
    }
}
