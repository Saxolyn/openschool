package com.openschool.common.file.result;

import com.openschool.domain.common.file.FileCategory;
import com.openschool.domain.common.file.FileMetadata;
import com.openschool.domain.common.file.FileStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UploadResultTest {

    @Test
    void success_ShouldCreateSuccessfulResult_WithMetadataOnly() {
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
        long processingTime = 150L;

        // When
        UploadResult result = UploadResult.success(metadata, processingTime);

        // Then
        assertTrue(result.isSuccessful());
        assertEquals(metadata, result.getFileMetadata());
        assertEquals(processingTime, result.getProcessingTimeMs());
        assertNotNull(result.getUploadedAt());
        assertNull(result.getErrorMessage());
        assertNull(result.getDownloadUrl());
        assertNull(result.getThumbnailUrl());
    }

    @Test
    void success_ShouldCreateSuccessfulResult_WithUrls() {
        // Given
        FileMetadata metadata = FileMetadata.builder()
                .id(UUID.randomUUID())
                .originalFileName("image.jpg")
                .contentType("image/jpeg")
                .fileSize(2048L)
                .category(FileCategory.IMAGE)
                .status(FileStatus.UPLOADED)
                .uploadedBy(UUID.randomUUID())
                .uploadedAt(LocalDateTime.now())
                .build();
        String downloadUrl = "/api/files/download/123";
        String thumbnailUrl = "/api/files/thumbnail/123";
        long processingTime = 200L;

        // When
        UploadResult result = UploadResult.success(metadata, downloadUrl, thumbnailUrl, processingTime);

        // Then
        assertTrue(result.isSuccessful());
        assertEquals(metadata, result.getFileMetadata());
        assertEquals(downloadUrl, result.getDownloadUrl());
        assertEquals(thumbnailUrl, result.getThumbnailUrl());
        assertEquals(processingTime, result.getProcessingTimeMs());
        assertNotNull(result.getUploadedAt());
        assertNull(result.getErrorMessage());
    }

    @Test
    void failure_ShouldCreateFailedResult() {
        // Given
        String errorMessage = "Upload failed due to invalid file type";

        // When
        UploadResult result = UploadResult.failure(errorMessage);

        // Then
        assertFalse(result.isSuccessful());
        assertEquals(errorMessage, result.getErrorMessage());
        assertNotNull(result.getUploadedAt());
        assertNull(result.getFileMetadata());
        assertNull(result.getDownloadUrl());
        assertNull(result.getThumbnailUrl());
        assertEquals(0L, result.getProcessingTimeMs());
    }
}
