package com.openschool.common.excel.result;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ExportResultTest {

    @Test
    void success_ShouldCreateSuccessfulResult() {
        // Given
        byte[] fileContent = "excel file content".getBytes();
        String fileName = "export.xlsx";
        int recordCount = 100;
        long processingTime = 250L;

        // When
        ExportResult result = ExportResult.success(fileContent, fileName, recordCount, processingTime);

        // Then
        assertTrue(result.isSuccessful());
        assertArrayEquals(fileContent, result.getFileContent());
        assertEquals(fileName, result.getFileName());
        assertEquals(recordCount, result.getRecordCount());
        assertEquals(processingTime, result.getProcessingTimeMs());
        assertNotNull(result.getExportedAt());
        assertNull(result.getErrorMessage());
    }

    @Test
    void failure_ShouldCreateFailedResult() {
        // Given
        String errorMessage = "Export failed due to data processing error";

        // When
        ExportResult result = ExportResult.failure(errorMessage);

        // Then
        assertFalse(result.isSuccessful());
        assertEquals(errorMessage, result.getErrorMessage());
        assertNotNull(result.getExportedAt());
        assertNull(result.getFileContent());
        assertNull(result.getFileName());
        assertEquals(0, result.getRecordCount());
        assertEquals(0L, result.getProcessingTimeMs());
    }

    @Test
    void success_ShouldHandleEmptyFileContent() {
        // Given
        byte[] emptyContent = new byte[0];
        String fileName = "empty.xlsx";
        int recordCount = 0;
        long processingTime = 50L;

        // When
        ExportResult result = ExportResult.success(emptyContent, fileName, recordCount, processingTime);

        // Then
        assertTrue(result.isSuccessful());
        assertArrayEquals(emptyContent, result.getFileContent());
        assertEquals(fileName, result.getFileName());
        assertEquals(recordCount, result.getRecordCount());
        assertEquals(processingTime, result.getProcessingTimeMs());
        assertNotNull(result.getExportedAt());
        assertNull(result.getErrorMessage());
    }

    @Test
    void success_ShouldHandleLargeRecordCount() {
        // Given
        byte[] fileContent = "large excel file".getBytes();
        String fileName = "large_export.xlsx";
        int recordCount = 1000000; // 1 million records
        long processingTime = 5000L; // 5 seconds

        // When
        ExportResult result = ExportResult.success(fileContent, fileName, recordCount, processingTime);

        // Then
        assertTrue(result.isSuccessful());
        assertEquals(recordCount, result.getRecordCount());
        assertEquals(processingTime, result.getProcessingTimeMs());
    }

    @Test
    void failure_ShouldHandleNullErrorMessage() {
        // Given
        String errorMessage = null;

        // When
        ExportResult result = ExportResult.failure(errorMessage);

        // Then
        assertFalse(result.isSuccessful());
        assertNull(result.getErrorMessage());
        assertNotNull(result.getExportedAt());
    }

    @Test
    void failure_ShouldHandleEmptyErrorMessage() {
        // Given
        String errorMessage = "";

        // When
        ExportResult result = ExportResult.failure(errorMessage);

        // Then
        assertFalse(result.isSuccessful());
        assertEquals("", result.getErrorMessage());
        assertNotNull(result.getExportedAt());
    }
}
