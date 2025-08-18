package com.openschool.common.file.result;

import com.openschool.domain.common.file.FileCategory;
import com.openschool.domain.common.file.FileMetadata;
import com.openschool.domain.common.file.FileStatus;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StreamResultTest {

    @Test
    void success_ShouldCreateSuccessfulResult_ForFullContent() {
        // Given
        FileMetadata metadata = FileMetadata.builder()
                .id(UUID.randomUUID())
                .originalFileName("video.mp4")
                .contentType("video/mp4")
                .fileSize(1024L)
                .category(FileCategory.VIDEO)
                .status(FileStatus.UPLOADED)
                .uploadedBy(UUID.randomUUID())
                .uploadedAt(LocalDateTime.now())
                .build();

        InputStream inputStream = new ByteArrayInputStream("video content".getBytes());
        long contentLength = 1024L;

        // When
        StreamResult result = StreamResult.success(inputStream, metadata, contentLength);

        // Then
        assertTrue(result.isSuccessful());
        assertEquals(inputStream, result.getInputStream());
        assertEquals(metadata, result.getFileMetadata());
        assertEquals(contentLength, result.getContentLength());
        assertEquals(0, result.getRangeStart());
        assertEquals(contentLength - 1, result.getRangeEnd());
        assertFalse(result.isPartialContent());
        assertNotNull(result.getAccessedAt());
        assertNull(result.getErrorMessage());
        assertNull(result.getContentRange());
    }

    @Test
    void successPartial_ShouldCreateSuccessfulResult_ForPartialContent() {
        // Given
        FileMetadata metadata = FileMetadata.builder()
                .id(UUID.randomUUID())
                .originalFileName("video.mp4")
                .contentType("video/mp4")
                .fileSize(1024L)
                .category(FileCategory.VIDEO)
                .status(FileStatus.UPLOADED)
                .uploadedBy(UUID.randomUUID())
                .uploadedAt(LocalDateTime.now())
                .build();

        InputStream inputStream = new ByteArrayInputStream("partial content".getBytes());
        long contentLength = 100L;
        long rangeStart = 200L;
        long rangeEnd = 299L;

        // When
        StreamResult result = StreamResult.successPartial(inputStream, metadata, contentLength, rangeStart, rangeEnd);

        // Then
        assertTrue(result.isSuccessful());
        assertEquals(inputStream, result.getInputStream());
        assertEquals(metadata, result.getFileMetadata());
        assertEquals(contentLength, result.getContentLength());
        assertEquals(rangeStart, result.getRangeStart());
        assertEquals(rangeEnd, result.getRangeEnd());
        assertTrue(result.isPartialContent());
        assertNotNull(result.getAccessedAt());
        assertNull(result.getErrorMessage());
        
        String expectedContentRange = "bytes 200-299/1024";
        assertEquals(expectedContentRange, result.getContentRange());
    }

    @Test
    void failure_ShouldCreateFailedResult() {
        // Given
        String errorMessage = "Stream failed";

        // When
        StreamResult result = StreamResult.failure(errorMessage);

        // Then
        assertFalse(result.isSuccessful());
        assertEquals(errorMessage, result.getErrorMessage());
        assertNotNull(result.getAccessedAt());
        assertNull(result.getInputStream());
        assertNull(result.getFileMetadata());
        assertEquals(0L, result.getContentLength());
        assertEquals(0L, result.getRangeStart());
        assertEquals(0L, result.getRangeEnd());
        assertFalse(result.isPartialContent());
        assertNull(result.getContentRange());
    }

    @Test
    void getContentRange_ShouldReturnNull_WhenNotPartialContent() {
        // Given
        FileMetadata metadata = FileMetadata.builder()
                .id(UUID.randomUUID())
                .originalFileName("video.mp4")
                .fileSize(1024L)
                .build();

        InputStream inputStream = new ByteArrayInputStream("content".getBytes());
        StreamResult result = StreamResult.success(inputStream, metadata, 1024L);

        // When
        String contentRange = result.getContentRange();

        // Then
        assertNull(contentRange);
    }
}
