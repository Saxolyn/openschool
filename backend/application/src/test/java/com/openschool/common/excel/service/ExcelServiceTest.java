package com.openschool.common.excel.service;

import com.openschool.common.excel.command.ExportExcelCommand;
import com.openschool.common.excel.command.ImportExcelCommand;
import com.openschool.common.excel.exception.ExcelProcessingException;
import com.openschool.common.excel.exception.ExcelValidationException;
import com.openschool.common.excel.port.ExcelReaderPort;
import com.openschool.common.excel.port.ExcelWriterPort;
import com.openschool.common.excel.result.ExportResult;
import com.openschool.common.excel.result.ImportResult;
import com.openschool.domain.common.excel.ExcelColumn;
import com.openschool.domain.common.excel.ExcelData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class ExcelServiceTest {

    private ExcelReaderPort excelReaderPort;
    private ExcelWriterPort excelWriterPort;
    private ExcelService<TestEntity> excelService;

    // Test entity for demonstration
    static class TestEntity {
        private String name;
        private String email;
        private Integer age;

        public TestEntity() {}

        public TestEntity(String name, String email, Integer age) {
            this.name = name;
            this.email = email;
            this.age = age;
        }

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
    }

    @BeforeEach
    void setUp() {
        excelReaderPort = mock(ExcelReaderPort.class);
        excelWriterPort = mock(ExcelWriterPort.class);
        
        // Create columns definition for TestEntity
        List<ExcelColumn> columns = Arrays.asList(
                new ExcelColumn("Name", "name", "string", true),
                new ExcelColumn("Email", "email", "string", true),
                new ExcelColumn("Age", "age", "integer", false)
        );

        excelService = new ExcelService<TestEntity>(excelReaderPort, excelWriterPort, columns) {
            @Override
            protected TestEntity convertRowToEntity(Map<String, Object> rowData) {
                TestEntity entity = new TestEntity();
                entity.setName((String) rowData.get("name"));
                entity.setEmail((String) rowData.get("email"));
                entity.setAge((Integer) rowData.get("age"));
                return entity;
            }

            @Override
            protected Map<String, Object> convertEntityToRow(TestEntity entity) {
                return Map.of(
                        "name", entity.getName(),
                        "email", entity.getEmail(),
                        "age", entity.getAge()
                );
            }
        };
    }

    @Test
    void importFromExcel_ShouldReturnSuccessResult_WhenValidData() {
        // Given
        byte[] excelData = "mock excel data".getBytes();
        ImportExcelCommand command = new ImportExcelCommand(excelData, "test.xlsx");

        ExcelData mockExcelData = ExcelData.builder()
                .headers(Arrays.asList("Name", "Email", "Age"))
                .rows(Arrays.asList(
                        Arrays.asList("John Doe", "john@example.com", "25"),
                        Arrays.asList("Jane Smith", "jane@example.com", "30")
                ))
                .build();

        when(excelReaderPort.readExcel(excelData)).thenReturn(mockExcelData);

        // When
        ImportResult<TestEntity> result = excelService.importFromExcel(command);

        // Then
        assertTrue(result.isSuccessful());
        assertEquals(2, result.getSuccessfulRecords().size());
        assertEquals(0, result.getFailedRecords().size());
        assertEquals(2, result.getTotalProcessed());

        TestEntity firstEntity = result.getSuccessfulRecords().get(0);
        assertEquals("John Doe", firstEntity.getName());
        assertEquals("john@example.com", firstEntity.getEmail());
        assertEquals(25, firstEntity.getAge());
    }

    @Test
    void importFromExcel_ShouldReturnPartialSuccess_WhenSomeRowsInvalid() {
        // Given
        byte[] excelData = "mock excel data".getBytes();
        ImportExcelCommand command = new ImportExcelCommand(excelData, "test.xlsx");

        ExcelData mockExcelData = ExcelData.builder()
                .headers(Arrays.asList("Name", "Email", "Age"))
                .rows(Arrays.asList(
                        Arrays.asList("John Doe", "john@example.com", "25"),
                        Arrays.asList("", "invalid-email", "not_a_number"), // Invalid row
                        Arrays.asList("Jane Smith", "jane@example.com", "30")
                ))
                .build();

        when(excelReaderPort.readExcel(excelData)).thenReturn(mockExcelData);

        // When
        ImportResult<TestEntity> result = excelService.importFromExcel(command);

        // Then
        assertTrue(result.isSuccessful()); // Still successful overall
        assertEquals(2, result.getSuccessfulRecords().size());
        assertEquals(1, result.getFailedRecords().size());
        assertEquals(3, result.getTotalProcessed());

        // Check failed record
        ImportResult.FailedRecord failedRecord = result.getFailedRecords().get(0);
        assertEquals(2, failedRecord.getRowNumber()); // 0-based index + 1
        assertFalse(failedRecord.getErrors().isEmpty());
    }

    @Test
    void importFromExcel_ShouldThrowException_WhenMissingRequiredColumns() {
        // Given
        byte[] excelData = "mock excel data".getBytes();
        ImportExcelCommand command = new ImportExcelCommand(excelData, "test.xlsx");

        ExcelData mockExcelData = ExcelData.builder()
                .headers(Arrays.asList("Name", "Age")) // Missing required "Email" column
                .rows(Arrays.asList(
                        Arrays.asList("John Doe", "25")
                ))
                .build();

        when(excelReaderPort.readExcel(excelData)).thenReturn(mockExcelData);

        // When & Then
        ExcelValidationException exception = assertThrows(ExcelValidationException.class,
                () -> excelService.importFromExcel(command));
        
        assertTrue(exception.getMessage().contains("Missing required columns"));
    }

    @Test
    void importFromExcel_ShouldThrowException_WhenReaderFails() {
        // Given
        byte[] excelData = "invalid excel data".getBytes();
        ImportExcelCommand command = new ImportExcelCommand(excelData, "test.xlsx");

        when(excelReaderPort.readExcel(excelData))
                .thenThrow(new ExcelProcessingException("Failed to read Excel file"));

        // When & Then
        ExcelProcessingException exception = assertThrows(ExcelProcessingException.class,
                () -> excelService.importFromExcel(command));
        
        assertEquals("Failed to read Excel file", exception.getMessage());
    }

    @Test
    void exportToExcel_ShouldReturnSuccessResult_WhenValidData() {
        // Given
        List<TestEntity> entities = Arrays.asList(
                new TestEntity("John Doe", "john@example.com", 25),
                new TestEntity("Jane Smith", "jane@example.com", 30)
        );

        ExportExcelCommand<TestEntity> command = ExportExcelCommand.<TestEntity>builder()
                .data(entities)
                .fileName("export.xlsx")
                .sheetName("Users")
                .build();

        byte[] mockExcelBytes = "mock excel output".getBytes();
        when(excelWriterPort.writeExcel(any(ExcelData.class), anyString())).thenReturn(mockExcelBytes);

        // When
        ExportResult result = excelService.exportToExcel(command);

        // Then
        assertTrue(result.isSuccessful());
        assertNotNull(result.getFileContent());
        assertArrayEquals(mockExcelBytes, result.getFileContent());
        assertEquals("export.xlsx", result.getFileName());
        assertEquals(2, result.getRecordCount());

        verify(excelWriterPort).writeExcel(any(ExcelData.class), eq("Users"));
    }

    @Test
    void exportToExcel_ShouldThrowException_WhenWriterFails() {
        // Given
        List<TestEntity> entities = Arrays.asList(
                new TestEntity("John Doe", "john@example.com", 25)
        );

        ExportExcelCommand<TestEntity> command = ExportExcelCommand.<TestEntity>builder()
                .data(entities)
                .fileName("export.xlsx")
                .sheetName("Users")
                .build();

        when(excelWriterPort.writeExcel(any(ExcelData.class), anyString()))
                .thenThrow(new ExcelProcessingException("Failed to write Excel file"));

        // When & Then
        ExcelProcessingException exception = assertThrows(ExcelProcessingException.class,
                () -> excelService.exportToExcel(command));
        
        assertEquals("Failed to write Excel file", exception.getMessage());
    }

    @Test
    void exportAllToExcel_ShouldReturnSuccessResult_WhenCalled() {
        // Given
        String fileName = "all_users.xlsx";
        byte[] mockExcelBytes = "mock excel output".getBytes();
        when(excelWriterPort.writeExcel(any(ExcelData.class), anyString())).thenReturn(mockExcelBytes);

        // When
        ExportResult result = excelService.exportAllToExcel(fileName);

        // Then
        assertTrue(result.isSuccessful());
        assertNotNull(result.getFileContent());
        assertEquals(fileName, result.getFileName());
    }

    @Test
    void generateImportTemplate_ShouldReturnTemplateBytes() {
        // Given
        byte[] mockTemplateBytes = "mock template".getBytes();
        when(excelWriterPort.writeExcel(any(ExcelData.class), anyString())).thenReturn(mockTemplateBytes);

        // When
        byte[] result = excelService.generateImportTemplate();

        // Then
        assertNotNull(result);
        assertArrayEquals(mockTemplateBytes, result);
        verify(excelWriterPort).writeExcel(any(ExcelData.class), eq("Template"));
    }

    @Test
    void validateImportFile_ShouldNotThrowException_WhenValidFile() {
        // Given
        byte[] excelData = "valid excel data".getBytes();
        ImportExcelCommand command = new ImportExcelCommand(excelData, "test.xlsx");

        ExcelData mockExcelData = ExcelData.builder()
                .headers(Arrays.asList("Name", "Email", "Age"))
                .rows(Arrays.asList(
                        Arrays.asList("John Doe", "john@example.com", "25")
                ))
                .build();

        when(excelReaderPort.readExcel(excelData)).thenReturn(mockExcelData);

        // When & Then
        assertDoesNotThrow(() -> excelService.validateImportFile(command));
    }

    @Test
    void validateImportFile_ShouldThrowException_WhenInvalidFile() {
        // Given
        byte[] excelData = "invalid excel data".getBytes();
        ImportExcelCommand command = new ImportExcelCommand(excelData, "test.xlsx");

        when(excelReaderPort.readExcel(excelData))
                .thenThrow(new ExcelProcessingException("Invalid Excel format"));

        // When & Then
        ExcelProcessingException exception = assertThrows(ExcelProcessingException.class,
                () -> excelService.validateImportFile(command));
        
        assertEquals("Invalid Excel format", exception.getMessage());
    }
}
