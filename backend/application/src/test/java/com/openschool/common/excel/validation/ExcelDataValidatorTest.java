package com.openschool.common.excel.validation;

import com.openschool.domain.common.excel.ExcelColumn;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExcelDataValidatorTest {

    @Test
    void validateCellValue_ShouldReturnNoErrors_WhenValidStringValue() {
        // Given
        ExcelColumn column = new ExcelColumn("Name", "name", "string", true);
        Object value = "John Doe";
        int rowNumber = 1;

        // When
        List<String> errors = ExcelDataValidator.validateCellValue(value, column, rowNumber);

        // Then
        assertTrue(errors.isEmpty());
    }

    @Test
    void validateCellValue_ShouldReturnError_WhenRequiredFieldIsEmpty() {
        // Given
        ExcelColumn column = new ExcelColumn("Name", "name", "string", true);
        Object value = "";
        int rowNumber = 1;

        // When
        List<String> errors = ExcelDataValidator.validateCellValue(value, column, rowNumber);

        // Then
        assertFalse(errors.isEmpty());
        assertEquals("Row 1: Name is required", errors.get(0));
    }

    @Test
    void validateCellValue_ShouldReturnError_WhenRequiredFieldIsNull() {
        // Given
        ExcelColumn column = new ExcelColumn("Name", "name", "string", true);
        Object value = null;
        int rowNumber = 2;

        // When
        List<String> errors = ExcelDataValidator.validateCellValue(value, column, rowNumber);

        // Then
        assertFalse(errors.isEmpty());
        assertEquals("Row 2: Name is required", errors.get(0));
    }

    @Test
    void validateCellValue_ShouldReturnNoErrors_WhenOptionalFieldIsEmpty() {
        // Given
        ExcelColumn column = new ExcelColumn("Description", "description", "string", false);
        Object value = "";
        int rowNumber = 1;

        // When
        List<String> errors = ExcelDataValidator.validateCellValue(value, column, rowNumber);

        // Then
        assertTrue(errors.isEmpty());
    }

    @Test
    void validateCellValue_ShouldReturnNoErrors_WhenValidIntegerValue() {
        // Given
        ExcelColumn column = new ExcelColumn("Age", "age", "integer", true);
        Object value = "25";
        int rowNumber = 1;

        // When
        List<String> errors = ExcelDataValidator.validateCellValue(value, column, rowNumber);

        // Then
        assertTrue(errors.isEmpty());
    }

    @Test
    void validateCellValue_ShouldReturnError_WhenInvalidIntegerValue() {
        // Given
        ExcelColumn column = new ExcelColumn("Age", "age", "integer", true);
        Object value = "not_a_number";
        int rowNumber = 1;

        // When
        List<String> errors = ExcelDataValidator.validateCellValue(value, column, rowNumber);

        // Then
        assertFalse(errors.isEmpty());
        assertEquals("Row 1: Age must be a valid integer", errors.get(0));
    }

    @Test
    void validateCellValue_ShouldReturnNoErrors_WhenValidDateValue() {
        // Given
        ExcelColumn column = new ExcelColumn("Birth Date", "birthDate", "date", true);
        Object value = "1990-01-01";
        int rowNumber = 1;

        // When
        List<String> errors = ExcelDataValidator.validateCellValue(value, column, rowNumber);

        // Then
        assertTrue(errors.isEmpty());
    }

    @Test
    void validateCellValue_ShouldReturnError_WhenInvalidDateValue() {
        // Given
        ExcelColumn column = new ExcelColumn("Birth Date", "birthDate", "date", true);
        Object value = "invalid_date";
        int rowNumber = 1;

        // When
        List<String> errors = ExcelDataValidator.validateCellValue(value, column, rowNumber);

        // Then
        assertFalse(errors.isEmpty());
        assertTrue(errors.get(0).contains("must be a valid date"));
    }

    @Test
    void validateCellValue_ShouldReturnNoErrors_WhenValidBooleanValue() {
        // Given
        ExcelColumn column = new ExcelColumn("Active", "active", "boolean", true);
        Object value = "true";
        int rowNumber = 1;

        // When
        List<String> errors = ExcelDataValidator.validateCellValue(value, column, rowNumber);

        // Then
        assertTrue(errors.isEmpty());
    }

    @Test
    void validateCellValue_ShouldReturnError_WhenInvalidBooleanValue() {
        // Given
        ExcelColumn column = new ExcelColumn("Active", "active", "boolean", true);
        Object value = "maybe";
        int rowNumber = 1;

        // When
        List<String> errors = ExcelDataValidator.validateCellValue(value, column, rowNumber);

        // Then
        assertFalse(errors.isEmpty());
        assertTrue(errors.get(0).contains("must be true/false, yes/no, or 1/0"));
    }

    @Test
    void validateCellValue_ShouldReturnError_WhenPatternDoesNotMatch() {
        // Given
        ExcelColumn column = ExcelColumn.builder()
                .name("Email")
                .fieldName("email")
                .dataType("string")
                .required(true)
                .validationPattern("^[A-Za-z0-9+_.-]+@(.+)$")
                .build();
        Object value = "invalid_email";
        int rowNumber = 1;

        // When
        List<String> errors = ExcelDataValidator.validateCellValue(value, column, rowNumber);

        // Then
        assertFalse(errors.isEmpty());
        assertTrue(errors.get(0).contains("does not match required pattern"));
    }

    @Test
    void validateRequiredColumns_ShouldNotThrowException_WhenAllRequiredColumnsPresent() {
        // Given
        List<String> actualColumns = Arrays.asList("Name", "Email", "Age");
        List<ExcelColumn> requiredColumns = Arrays.asList(
                new ExcelColumn("Name", "name", "string", true),
                new ExcelColumn("Email", "email", "string", true),
                new ExcelColumn("Age", "age", "integer", false)
        );

        // When & Then
        assertDoesNotThrow(() -> ExcelDataValidator.validateRequiredColumns(actualColumns, requiredColumns));
    }

    @Test
    void validateRequiredColumns_ShouldThrowException_WhenRequiredColumnsMissing() {
        // Given
        List<String> actualColumns = Arrays.asList("Name", "Age");
        List<ExcelColumn> requiredColumns = Arrays.asList(
                new ExcelColumn("Name", "name", "string", true),
                new ExcelColumn("Email", "email", "string", true),
                new ExcelColumn("Age", "age", "integer", false)
        );

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ExcelDataValidator.validateRequiredColumns(actualColumns, requiredColumns));
        
        assertTrue(exception.getMessage().contains("Missing required columns"));
        assertTrue(exception.getMessage().contains("Email"));
    }

    @Test
    void validateRequiredColumns_ShouldNotThrowException_WhenOnlyOptionalColumnsMissing() {
        // Given
        List<String> actualColumns = Arrays.asList("Name", "Email");
        List<ExcelColumn> requiredColumns = Arrays.asList(
                new ExcelColumn("Name", "name", "string", true),
                new ExcelColumn("Email", "email", "string", true),
                new ExcelColumn("Age", "age", "integer", false)
        );

        // When & Then
        assertDoesNotThrow(() -> ExcelDataValidator.validateRequiredColumns(actualColumns, requiredColumns));
    }
}
