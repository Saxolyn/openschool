package com.openschool.common.excel.converter;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class ExcelDataConverterTest {

    @Test
    void convertValue_ShouldReturnString_WhenTargetTypeIsString() {
        // Given
        Object value = "test value";
        String targetType = "string";

        // When
        Object result = ExcelDataConverter.convertValue(value, targetType);

        // Then
        assertEquals("test value", result);
        assertInstanceOf(String.class, result);
    }

    @Test
    void convertValue_ShouldReturnInteger_WhenTargetTypeIsInteger() {
        // Given
        Object value = "123";
        String targetType = "integer";

        // When
        Object result = ExcelDataConverter.convertValue(value, targetType);

        // Then
        assertEquals(123, result);
        assertInstanceOf(Integer.class, result);
    }

    @Test
    void convertValue_ShouldThrowException_WhenInvalidIntegerValue() {
        // Given
        Object value = "not_a_number";
        String targetType = "integer";

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ExcelDataConverter.convertValue(value, targetType));
        
        assertTrue(exception.getMessage().contains("Cannot convert 'not_a_number' to Integer"));
    }

    @Test
    void convertValue_ShouldReturnLong_WhenTargetTypeIsLong() {
        // Given
        Object value = "123456789";
        String targetType = "long";

        // When
        Object result = ExcelDataConverter.convertValue(value, targetType);

        // Then
        assertEquals(123456789L, result);
        assertInstanceOf(Long.class, result);
    }

    @Test
    void convertValue_ShouldReturnDouble_WhenTargetTypeIsDouble() {
        // Given
        Object value = "123.45";
        String targetType = "double";

        // When
        Object result = ExcelDataConverter.convertValue(value, targetType);

        // Then
        assertEquals(123.45, result);
        assertInstanceOf(Double.class, result);
    }

    @Test
    void convertValue_ShouldReturnTrue_WhenBooleanValueIsTrue() {
        // Given
        Object value = "true";
        String targetType = "boolean";

        // When
        Object result = ExcelDataConverter.convertValue(value, targetType);

        // Then
        assertEquals(true, result);
        assertInstanceOf(Boolean.class, result);
    }

    @Test
    void convertValue_ShouldReturnTrue_WhenBooleanValueIsYes() {
        // Given
        Object value = "yes";
        String targetType = "boolean";

        // When
        Object result = ExcelDataConverter.convertValue(value, targetType);

        // Then
        assertEquals(true, result);
    }

    @Test
    void convertValue_ShouldReturnTrue_WhenBooleanValueIsOne() {
        // Given
        Object value = "1";
        String targetType = "boolean";

        // When
        Object result = ExcelDataConverter.convertValue(value, targetType);

        // Then
        assertEquals(true, result);
    }

    @Test
    void convertValue_ShouldReturnFalse_WhenBooleanValueIsFalse() {
        // Given
        Object value = "false";
        String targetType = "boolean";

        // When
        Object result = ExcelDataConverter.convertValue(value, targetType);

        // Then
        assertEquals(false, result);
    }

    @Test
    void convertValue_ShouldReturnLocalDate_WhenValidDateString() {
        // Given
        Object value = "2024-01-01";
        String targetType = "date";

        // When
        Object result = ExcelDataConverter.convertValue(value, targetType);

        // Then
        assertEquals(LocalDate.of(2024, 1, 1), result);
        assertInstanceOf(LocalDate.class, result);
    }

    @Test
    void convertValue_ShouldReturnLocalDate_WhenValidDateStringDifferentFormat() {
        // Given
        Object value = "01/01/2024";
        String targetType = "localdate";

        // When
        Object result = ExcelDataConverter.convertValue(value, targetType);

        // Then
        assertEquals(LocalDate.of(2024, 1, 1), result);
    }

    @Test
    void convertValue_ShouldThrowException_WhenInvalidDateString() {
        // Given
        Object value = "invalid_date";
        String targetType = "date";

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ExcelDataConverter.convertValue(value, targetType));
        
        assertTrue(exception.getMessage().contains("Cannot convert 'invalid_date' to LocalDate"));
    }

    @Test
    void convertValue_ShouldReturnNull_WhenValueIsNull() {
        // Given
        Object value = null;
        String targetType = "string";

        // When
        Object result = ExcelDataConverter.convertValue(value, targetType);

        // Then
        assertNull(result);
    }

    @Test
    void convertValue_ShouldReturnNull_WhenValueIsEmptyString() {
        // Given
        Object value = "";
        String targetType = "string";

        // When
        Object result = ExcelDataConverter.convertValue(value, targetType);

        // Then
        assertNull(result);
    }

    @Test
    void convertValue_ShouldReturnNull_WhenValueIsWhitespace() {
        // Given
        Object value = "   ";
        String targetType = "string";

        // When
        Object result = ExcelDataConverter.convertValue(value, targetType);

        // Then
        assertNull(result);
    }

    @Test
    void formatValue_ShouldReturnEmptyString_WhenValueIsNull() {
        // Given
        Object value = null;

        // When
        String result = ExcelDataConverter.formatValue(value);

        // Then
        assertEquals("", result);
    }

    @Test
    void formatValue_ShouldReturnFormattedDate_WhenValueIsLocalDate() {
        // Given
        Object value = LocalDate.of(2024, 1, 1);

        // When
        String result = ExcelDataConverter.formatValue(value);

        // Then
        assertEquals("2024-01-01", result);
    }

    @Test
    void formatValue_ShouldReturnYes_WhenValueIsTrueBoolean() {
        // Given
        Object value = true;

        // When
        String result = ExcelDataConverter.formatValue(value);

        // Then
        assertEquals("Yes", result);
    }

    @Test
    void formatValue_ShouldReturnNo_WhenValueIsFalseBoolean() {
        // Given
        Object value = false;

        // When
        String result = ExcelDataConverter.formatValue(value);

        // Then
        assertEquals("No", result);
    }

    @Test
    void formatValue_ShouldReturnToString_WhenValueIsOtherType() {
        // Given
        Object value = 123;

        // When
        String result = ExcelDataConverter.formatValue(value);

        // Then
        assertEquals("123", result);
    }
}
