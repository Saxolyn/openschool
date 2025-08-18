package com.openschool.common.file.command;

import com.openschool.domain.common.file.FileCategory;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UploadFileCommandTest {

    @Test
    void builder_ShouldCreateCommandWithAllFields() {
        // Given
        byte[] fileData = "test file content".getBytes();
        String originalFileName = "test.pdf";
        String contentType = "application/pdf";
        FileCategory category = FileCategory.DOCUMENT;
        UUID uploadedBy = UUID.randomUUID();
        String description = "Test document";
        boolean isPublic = true;
        Map<String, Object> additionalMetadata = new HashMap<>();
        additionalMetadata.put("department", "HR");
        boolean validateFileType = true;
        boolean generateThumbnail = false;
        long maxFileSize = 5 * 1024 * 1024; // 5MB

        // When
        UploadFileCommand command = UploadFileCommand.builder()
                .fileData(fileData)
                .originalFileName(originalFileName)
                .contentType(contentType)
                .category(category)
                .uploadedBy(uploadedBy)
                .description(description)
                .isPublic(isPublic)
                .additionalMetadata(additionalMetadata)
                .validateFileType(validateFileType)
                .generateThumbnail(generateThumbnail)
                .maxFileSize(maxFileSize)
                .build();

        // Then
        assertArrayEquals(fileData, command.getFileData());
        assertEquals(originalFileName, command.getOriginalFileName());
        assertEquals(contentType, command.getContentType());
        assertEquals(category, command.getCategory());
        assertEquals(uploadedBy, command.getUploadedBy());
        assertEquals(description, command.getDescription());
        assertTrue(command.isPublic());
        assertEquals(additionalMetadata, command.getAdditionalMetadata());
        assertTrue(command.isValidateFileType());
        assertFalse(command.isGenerateThumbnail());
        assertEquals(maxFileSize, command.getMaxFileSize());
    }

    @Test
    void simpleConstructor_ShouldCreateCommandWithDefaults() {
        // Given
        byte[] fileData = "test file content".getBytes();
        String originalFileName = "test.pdf";
        String contentType = "application/pdf";
        FileCategory category = FileCategory.DOCUMENT;
        UUID uploadedBy = UUID.randomUUID();

        // When
        UploadFileCommand command = new UploadFileCommand(fileData, originalFileName, contentType, category, uploadedBy);

        // Then
        assertArrayEquals(fileData, command.getFileData());
        assertEquals(originalFileName, command.getOriginalFileName());
        assertEquals(contentType, command.getContentType());
        assertEquals(category, command.getCategory());
        assertEquals(uploadedBy, command.getUploadedBy());
        assertNull(command.getDescription());
        assertFalse(command.isPublic());
        assertNull(command.getAdditionalMetadata());
        assertTrue(command.isValidateFileType());
        assertFalse(command.isGenerateThumbnail());
        assertEquals(10 * 1024 * 1024, command.getMaxFileSize()); // Default 10MB
    }

    @Test
    void builder_ShouldHandleNullValues() {
        // Given
        byte[] fileData = "test content".getBytes();
        String originalFileName = "test.txt";
        String contentType = "text/plain";
        FileCategory category = FileCategory.OTHER;
        UUID uploadedBy = UUID.randomUUID();

        // When
        UploadFileCommand command = UploadFileCommand.builder()
                .fileData(fileData)
                .originalFileName(originalFileName)
                .contentType(contentType)
                .category(category)
                .uploadedBy(uploadedBy)
                .description(null)
                .additionalMetadata(null)
                .build();

        // Then
        assertArrayEquals(fileData, command.getFileData());
        assertEquals(originalFileName, command.getOriginalFileName());
        assertEquals(contentType, command.getContentType());
        assertEquals(category, command.getCategory());
        assertEquals(uploadedBy, command.getUploadedBy());
        assertNull(command.getDescription());
        assertNull(command.getAdditionalMetadata());
    }

    @Test
    void builder_ShouldHandleEmptyAdditionalMetadata() {
        // Given
        Map<String, Object> emptyMetadata = new HashMap<>();
        
        // When
        UploadFileCommand command = UploadFileCommand.builder()
                .fileData("content".getBytes())
                .originalFileName("test.txt")
                .contentType("text/plain")
                .category(FileCategory.OTHER)
                .uploadedBy(UUID.randomUUID())
                .additionalMetadata(emptyMetadata)
                .build();

        // Then
        assertNotNull(command.getAdditionalMetadata());
        assertTrue(command.getAdditionalMetadata().isEmpty());
    }

    @Test
    void builder_ShouldHandleDifferentFileCategories() {
        // Test each file category
        FileCategory[] categories = FileCategory.values();
        
        for (FileCategory category : categories) {
            // When
            UploadFileCommand command = UploadFileCommand.builder()
                    .fileData("content".getBytes())
                    .originalFileName("test.file")
                    .contentType("application/octet-stream")
                    .category(category)
                    .uploadedBy(UUID.randomUUID())
                    .build();

            // Then
            assertEquals(category, command.getCategory());
        }
    }
}
