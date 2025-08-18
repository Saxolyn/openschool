package com.openschool.common.file.result;

import com.openschool.domain.common.file.FileCategory;
import com.openschool.domain.common.file.FileData;
import com.openschool.domain.common.file.FileMetadata;
import com.openschool.domain.common.file.FileStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DownloadResultTest {

    @Test
    void success_ShouldCreateSuccessfulResult() {
        // Given
        FileMetadata metadata = FileMetadata.builder()
                .id(UUID.randomUUID())
                .originalFileName("test.pdf")
                .contentType("application/pdf")
                .fileSize(1024L)
                .category(FileCategory.DOCUMENT)
                .status(FileStatus.UPLOADED)
                .uploadedBy(UUID.randomUUID())
                .uploadedAt(LocalDateTime.now())
                .build();

        byte[] content = "test file content".getBytes();
        FileData fileData = FileData.builder()
                .metadata(metadata)
                .content(content)
                .build();

        long processingTime = 100L;

        // When
        DownloadResult result = DownloadResult.success(fileData, processingTime);

        // Then
        assertTrue(result.isSuccessful());
        assertEquals(fileData, result.getFileData());
        assertEquals(metadata, result.getMetadata());
        assertArrayEquals(content, result.getContent());
        assertEquals(processingTime, result.getProcessingTimeMs());
        assertNotNull(result.getAccessedAt());
        assertNull(result.getErrorMessage());
    }

    @Test
    void failure_ShouldCreateFailedResult() {
        // Given
        String errorMessage = "File not found";

        // When
        DownloadResult result = DownloadResult.failure(errorMessage);

        // Then
        assertFalse(result.isSuccessful());
        assertEquals(errorMessage, result.getErrorMessage());
        assertNotNull(result.getAccessedAt());
        assertNull(result.getFileData());
        assertNull(result.getMetadata());
        assertNull(result.getContent());
        assertEquals(0L, result.getProcessingTimeMs());
    }

    @Test
    void getMetadata_ShouldReturnNull_WhenFileDataIsNull() {
        // Given
        DownloadResult result = DownloadResult.failure("Error");

        // When
        FileMetadata metadata = result.getMetadata();

        // Then
        assertNull(metadata);
    }

    @Test
    void getContent_ShouldReturnNull_WhenFileDataIsNull() {
        // Given
        DownloadResult result = DownloadResult.failure("Error");

        // When
        byte[] content = result.getContent();

        // Then
        assertNull(content);
    }
}
