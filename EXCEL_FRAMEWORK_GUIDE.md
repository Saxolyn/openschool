# OpenSchool Excel Import/Export Framework

## Tổng quan

Framework này cung cấp một giải pháp common và có thể tái sử dụng cho việc import/export dữ liệu Excel trong OpenSchool project. Framework tuân theo **Clean Architecture pattern** với **separation of concerns** rõ ràng:

- **Domain Layer**: Pure business entities (không dependency)
- **Application Layer**: Business logic thuần túy (chỉ Java core)
- **Infrastructure Layer**: Framework implementations (Spring, Apache POI)

## Kiến trúc

### 1. Domain Layer (Pure Business Entities)

#### Value Objects
- `ExcelColumn` - Định nghĩa cột Excel với validation rules
- `ExcelData` - Dữ liệu Excel đã được đọc

### 2. Application Layer (Pure Java - Business Logic)

#### Command Objects
- `ImportExcelCommand` - Command cho import operations (byte[] data)
- `ExportExcelCommand<T>` - Command cho export operations

#### Result Objects
- `ImportResult<T>` - Kết quả import với success/error records
- `ExportResult` - Kết quả export với file content

#### Port Interfaces
- `ExcelReaderPort` - Interface cho đọc Excel files
- `ExcelWriterPort` - Interface cho tạo Excel files

#### Use Case Interfaces
- `ExcelImportUseCase<T>` - Generic interface cho import operations
- `ExcelExportUseCase<T>` - Generic interface cho export operations

#### Validation & Conversion (Pure Java)
- `ExcelDataValidator` - Validation logic thuần túy
- `ExcelDataConverter` - Type conversion utilities

#### Exception Classes
- `ExcelProcessingException` - Base exception
- `InvalidExcelFormatException` - Format errors
- `ExcelValidationException` - Validation errors

### 3. Infrastructure Layer (Framework Implementation)

#### Adapters
- `ExcelReaderAdapter` - Apache POI implementation của ExcelReaderPort
- `ExcelWriterAdapter` - Apache POI implementation của ExcelWriterPort

#### REST Controllers
- `ExcelController` - REST endpoints cho Excel operations

#### Configuration
- `ExcelConfig` - Spring configuration cho Excel components

## Cách sử dụng

### 1. Tạo Excel Service cho Entity

```java
@Service
public class StudentExcelService extends AbstractExcelService<Student> {
    
    private final StudentRepositoryPort studentRepository;
    
    public StudentExcelService(ExcelProcessorPort excelProcessor, 
                              StudentRepositoryPort studentRepository) {
        super(excelProcessor);
        this.studentRepository = studentRepository;
    }
    
    @Override
    protected List<ExcelColumnMapping> getColumnMappings() {
        return Arrays.asList(
            new ExcelColumnMapping("Student Code", "studentCode", String.class, true),
            new ExcelColumnMapping("First Name", "firstName", String.class, true),
            // ... other mappings
        );
    }
    
    @Override
    protected String getSheetName() {
        return "Students";
    }
    
    @Override
    protected Student convertRowToObject(Map<String, Object> rowData, 
                                       List<ExcelColumnMapping> mappings, 
                                       int rowNumber) {
        // Convert Excel row to Student object
    }
    
    @Override
    protected void validateRecord(Student record, int rowNumber) {
        // Business validation logic
    }
    
    @Override
    protected void processImportedRecords(List<Student> records) {
        // Save records to database
    }
}
```

### 2. Tạo REST Controller

```java
@RestController
@RequestMapping("/api/students/excel")
public class StudentExcelController {
    
    private final StudentExcelService studentExcelService;
    
    @PostMapping("/import")
    public ResponseEntity<ImportResult<Student>> importStudents(
            @RequestParam("file") MultipartFile file) {
        
        ImportExcelCommand command = new ImportExcelCommand(
            file, 
            studentExcelService.getColumnMappings()
        );
        
        ImportResult<Student> result = studentExcelService.importFromExcel(command);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportStudents() {
        ExportResult result = studentExcelService.exportAllToExcel("students.xlsx");
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, 
                   "attachment; filename=" + result.getFileName())
            .contentType(MediaType.parseMediaType(result.getContentType()))
            .body(result.getFileContent());
    }
    
    @GetMapping("/template")
    public ResponseEntity<byte[]> downloadTemplate() {
        byte[] template = studentExcelService.generateImportTemplate();
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, 
                   "attachment; filename=student_template.xlsx")
            .contentType(MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(template);
    }
}
```

### 3. Configuration

Thêm vào StudentConfig:

```java
@Bean
public StudentExcelService studentExcelService(
        ExcelProcessorPort excelProcessorPort,
        StudentRepositoryPort studentRepositoryPort) {
    return new StudentExcelService(excelProcessorPort, studentRepositoryPort);
}
```

## Features

### 1. Import Features
- ✅ Đọc Excel files (.xlsx, .xls)
- ✅ Validation dữ liệu với custom rules
- ✅ Type conversion tự động
- ✅ Error reporting chi tiết
- ✅ Batch processing
- ✅ Skip validation errors option

### 2. Export Features
- ✅ Generate Excel files với custom formatting
- ✅ Auto-size columns
- ✅ Header styling
- ✅ Template generation
- ✅ Large dataset support

### 3. Validation Features
- ✅ Required field validation
- ✅ Type validation (String, Integer, Date, Enum, etc.)
- ✅ Pattern validation với regex
- ✅ Business logic validation
- ✅ Duplicate checking

### 4. Error Handling
- ✅ Comprehensive exception hierarchy
- ✅ Row-level error reporting
- ✅ Global exception handling
- ✅ Validation error details

## Dependencies

Đã thêm vào `infrastructure/build.gradle`:

```gradle
// Apache POI for Excel processing
implementation 'org.apache.poi:poi:5.2.5'
implementation 'org.apache.poi:poi-ooxml:5.2.5'
```

## Supported Data Types

- `String`
- `Integer/int`
- `Long/long`
- `Double/double`
- `Boolean/boolean`
- `LocalDate`
- `Enum` types
- Custom objects (với custom converter)

## Best Practices

1. **Column Mapping**: Luôn define rõ ràng column mappings với validation rules
2. **Error Handling**: Implement proper business validation trong `validateRecord()`
3. **Performance**: Sử dụng batch processing cho large datasets
4. **Security**: Validate file types và size trước khi process
5. **Logging**: Log import/export activities cho audit trail

## Mở rộng

Framework có thể dễ dàng mở rộng cho:
- Guardian Excel operations
- Employee Excel operations
- Department Excel operations
- Bất kỳ entity nào khác

Chỉ cần extend `AbstractExcelService<T>` và implement các abstract methods.
