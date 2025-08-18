package com.openschool.infrastructure.adapter.in.rest.common;

import com.openschool.common.excel.command.ImportExcelCommand;
import com.openschool.common.excel.port.ExcelReaderPort;
import com.openschool.common.excel.port.ExcelWriterPort;
import com.openschool.domain.common.excel.ExcelColumn;
import com.openschool.domain.common.excel.ExcelData;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * REST Controller for Excel operations (Framework layer)
 */
@RestController
@RequestMapping("/api/excel")
public class ExcelController {
    
    private final ExcelReaderPort excelReaderPort;
    private final ExcelWriterPort excelWriterPort;
    
    public ExcelController(ExcelReaderPort excelReaderPort, ExcelWriterPort excelWriterPort) {
        this.excelReaderPort = excelReaderPort;
        this.excelWriterPort = excelWriterPort;
    }
    
    @PostMapping("/validate")
    public ResponseEntity<String> validateExcelFile(
            @RequestParam("file") MultipartFile file) throws IOException {
        
        // Convert MultipartFile to byte[] (Framework → Application boundary)
        byte[] fileData = file.getBytes();
        String fileName = file.getOriginalFilename();
        
        // Example column definitions
        List<ExcelColumn> columns = Arrays.asList(
            new ExcelColumn("Name", "name", "string", true),
            new ExcelColumn("Email", "email", "string", true),
            new ExcelColumn("Age", "age", "integer", false)
        );
        
        try {
            excelReaderPort.validateExcelStructure(fileData, fileName, columns);
            return ResponseEntity.ok("Excel file is valid");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Validation failed: " + e.getMessage());
        }
    }
    
    @PostMapping("/read")
    public ResponseEntity<ExcelData> readExcelFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "sheetName", required = false) String sheetName) throws IOException {
        
        // Convert MultipartFile to byte[] (Framework → Application boundary)
        byte[] fileData = file.getBytes();
        String fileName = file.getOriginalFilename();
        
        ExcelData data = excelReaderPort.readExcelData(fileData, fileName, sheetName, 0, 1);
        return ResponseEntity.ok(data);
    }
    
    @GetMapping("/template")
    public ResponseEntity<byte[]> downloadTemplate() {
        
        // Example column definitions
        List<ExcelColumn> columns = Arrays.asList(
            new ExcelColumn("Name", "name", "string", true),
            new ExcelColumn("Email", "email", "string", true),
            new ExcelColumn("Age", "age", "integer", false)
        );
        
        byte[] template = excelWriterPort.generateTemplate(columns, "Template");
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=template.xlsx")
                .contentType(MediaType.parseMediaType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(template);
    }
    
    @GetMapping("/sheets")
    public ResponseEntity<List<String>> getSheetNames(
            @RequestParam("file") MultipartFile file) throws IOException {
        
        byte[] fileData = file.getBytes();
        String fileName = file.getOriginalFilename();
        
        List<String> sheetNames = excelReaderPort.getSheetNames(fileData, fileName);
        return ResponseEntity.ok(sheetNames);
    }
}
