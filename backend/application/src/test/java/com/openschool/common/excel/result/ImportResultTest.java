package com.openschool.common.excel.result;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ImportResultTest {

    static class TestEntity {
        private String name;
        private String email;

        public TestEntity(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public String getName() { return name; }
        public String getEmail() { return email; }
    }

    @Test
    void success_ShouldCreateSuccessfulResult_WithNoFailures() {
        // Given
        List<TestEntity> successfulRecords = Arrays.asList(
                new TestEntity("John Doe", "john@example.com"),
                new TestEntity("Jane Smith", "jane@example.com")
        );
        List<ImportResult.FailedRecord> failedRecords = Arrays.asList();
        long processingTime = 150L;

        // When
        ImportResult<TestEntity> result = ImportResult.success(successfulRecords, failedRecords, processingTime);

        // Then
        assertTrue(result.isSuccessful());
        assertEquals(2, result.getSuccessfulRecords().size());
        assertEquals(0, result.getFailedRecords().size());
        assertEquals(2, result.getTotalProcessed());
        assertEquals(processingTime, result.getProcessingTimeMs());
        assertNotNull(result.getProcessedAt());
        assertNull(result.getErrorMessage());
    }

    @Test
    void success_ShouldCreateSuccessfulResult_WithSomeFailures() {
        // Given
        List<TestEntity> successfulRecords = Arrays.asList(
                new TestEntity("John Doe", "john@example.com")
        );
        List<ImportResult.FailedRecord> failedRecords = Arrays.asList(
                new ImportResult.FailedRecord(2, Arrays.asList("Name is required"), Arrays.asList("", "invalid-email"))
        );
        long processingTime = 200L;

        // When
        ImportResult<TestEntity> result = ImportResult.success(successfulRecords, failedRecords, processingTime);

        // Then
        assertTrue(result.isSuccessful());
        assertEquals(1, result.getSuccessfulRecords().size());
        assertEquals(1, result.getFailedRecords().size());
        assertEquals(2, result.getTotalProcessed());
        assertEquals(processingTime, result.getProcessingTimeMs());
        assertNotNull(result.getProcessedAt());
        assertNull(result.getErrorMessage());
    }

    @Test
    void failure_ShouldCreateFailedResult() {
        // Given
        String errorMessage = "Failed to process Excel file";

        // When
        ImportResult<TestEntity> result = ImportResult.failure(errorMessage);

        // Then
        assertFalse(result.isSuccessful());
        assertEquals(errorMessage, result.getErrorMessage());
        assertNotNull(result.getProcessedAt());
        assertTrue(result.getSuccessfulRecords().isEmpty());
        assertTrue(result.getFailedRecords().isEmpty());
        assertEquals(0, result.getTotalProcessed());
        assertEquals(0L, result.getProcessingTimeMs());
    }

    @Test
    void failedRecord_ShouldCreateFailedRecordCorrectly() {
        // Given
        int rowNumber = 5;
        List<String> errors = Arrays.asList("Name is required", "Invalid email format");
        List<Object> rowData = Arrays.asList("", "invalid-email", "25");

        // When
        ImportResult.FailedRecord failedRecord = new ImportResult.FailedRecord(rowNumber, errors, rowData);

        // Then
        assertEquals(rowNumber, failedRecord.getRowNumber());
        assertEquals(errors, failedRecord.getErrors());
        assertEquals(rowData, failedRecord.getRowData());
    }

    @Test
    void getTotalProcessed_ShouldReturnCorrectSum() {
        // Given
        List<TestEntity> successfulRecords = Arrays.asList(
                new TestEntity("John", "john@example.com"),
                new TestEntity("Jane", "jane@example.com")
        );
        List<ImportResult.FailedRecord> failedRecords = Arrays.asList(
                new ImportResult.FailedRecord(3, Arrays.asList("Error"), Arrays.asList("invalid")),
                new ImportResult.FailedRecord(4, Arrays.asList("Error"), Arrays.asList("invalid"))
        );

        ImportResult<TestEntity> result = ImportResult.success(successfulRecords, failedRecords, 100L);

        // When
        int totalProcessed = result.getTotalProcessed();

        // Then
        assertEquals(4, totalProcessed); // 2 successful + 2 failed
    }

    @Test
    void isSuccessful_ShouldReturnTrue_EvenWithSomeFailures() {
        // Given
        List<TestEntity> successfulRecords = Arrays.asList(
                new TestEntity("John", "john@example.com")
        );
        List<ImportResult.FailedRecord> failedRecords = Arrays.asList(
                new ImportResult.FailedRecord(2, Arrays.asList("Error"), Arrays.asList("invalid"))
        );

        // When
        ImportResult<TestEntity> result = ImportResult.success(successfulRecords, failedRecords, 100L);

        // Then
        assertTrue(result.isSuccessful()); // Still successful if some records processed successfully
    }
}
