# OpenSchool File Management Framework

## Tổng quan

Framework này cung cấp một giải pháp common và có thể tái sử dụng cho việc upload, download và stream file trong OpenSchool project. Framework tuân theo **Clean Architecture pattern** với **separation of concerns** rõ ràng.

## Kiến trúc

### 1. Domain Layer (Pure Business Entities)

#### Value Objects
- `FileMetadata` - Metadata của file với thông tin chi tiết
- `FileData` - Dữ liệu file kết hợp metadata và content
- `FileCategory` - Enum phân loại file
- `FileStatus` - Enum trạng thái file

### 2. Application Layer (Pure Java - Business Logic)

#### Command Objects
- `UploadFileCommand` - Command cho upload operations
- `DownloadFileCommand` - Command cho download operations  
- `StreamFileCommand` - Command cho streaming operations

#### Result Objects
- `UploadResult` - Kết quả upload với metadata và URLs
- `DownloadResult` - Kết quả download với file data
- `StreamResult` - Kết quả streaming với input stream

#### Port Interfaces
- `FileStoragePort` - Interface cho file storage operations
- `FileMetadataRepositoryPort` - Interface cho metadata persistence

#### Use Case Interfaces
- `UploadFileUseCase` - Interface cho upload operations
- `DownloadFileUseCase` - Interface cho download operations
- `StreamFileUseCase` - Interface cho streaming operations

#### Validation & Security (Pure Java)
- `FileValidator` - Validation logic thuần túy
- Access control logic trong service

#### Exception Classes
- `FileProcessingException` - Base exception
- `FileNotFoundException` - File not found errors
- `FileValidationException` - Validation errors
- `FileAccessDeniedException` - Access denied errors

### 3. Infrastructure Layer (Framework Implementation)

#### Adapters
- `LocalFileStorageAdapter` - Local file system implementation
- `FileMetadataRepositoryAdapter` - Database implementation (cần tạo)

#### REST Controllers
- `FileController` - REST endpoints cho file operations

#### Configuration
- `FileConfig` - Spring configuration cho file components

## Cách sử dụng

### 1. Upload File

```java
// REST API
POST /api/files/upload
Content-Type: multipart/form-data

Parameters:
- file: MultipartFile
- category: String (IMAGE, DOCUMENT, VIDEO, etc.)
- description: String (optional)
- isPublic: boolean (default: false)
- generateThumbnail: boolean (default: false)

Response:
{
  "fileMetadata": {
    "id": "uuid",
    "originalFileName": "document.pdf",
    "contentType": "application/pdf",
    "fileSize": 1024000,
    "category": "DOCUMENT",
    "uploadedAt": "2024-01-01T10:00:00"
  },
  "successful": true,
  "downloadUrl": "/api/files/{id}/download",
  "thumbnailUrl": "/api/files/{id}/thumbnail"
}
```

### 2. Download File

```java
// REST API
GET /api/files/{fileId}/download?userId={userId}

Response:
- Content-Disposition: attachment; filename="document.pdf"
- Content-Type: application/pdf
- Content-Length: 1024000
- Body: file content
```

### 3. Stream File (với Range Support)

```java
// REST API
GET /api/files/{fileId}/stream?userId={userId}
Range: bytes=0-1023

Response:
- Status: 206 Partial Content (nếu có Range)
- Content-Range: bytes 0-1023/1024000
- Accept-Ranges: bytes
- Content-Type: video/mp4
- Body: file stream
```

### 4. Programmatic Usage

```java
@Service
public class DocumentService {
    
    private final UploadFileUseCase uploadFileUseCase;
    
    public UploadResult uploadDocument(byte[] fileData, String fileName, UUID userId) {
        UploadFileCommand command = UploadFileCommand.builder()
                .fileData(fileData)
                .originalFileName(fileName)
                .contentType("application/pdf")
                .category(FileCategory.DOCUMENT)
                .uploadedBy(userId)
                .isPublic(false)
                .build();
                
        return uploadFileUseCase.uploadFile(command);
    }
}
```

## Features

### 1. Upload Features
- ✅ Multiple file format support
- ✅ File size validation per category
- ✅ File type validation
- ✅ Virus scanning ready
- ✅ Thumbnail generation for images
- ✅ Checksum calculation
- ✅ Unique file naming
- ✅ Directory organization by date

### 2. Download Features
- ✅ Access control
- ✅ Metadata tracking
- ✅ Last accessed time update
- ✅ Content-Type detection
- ✅ Proper HTTP headers

### 3. Streaming Features
- ✅ Range request support (HTTP 206)
- ✅ Partial content streaming
- ✅ Video/audio streaming optimized
- ✅ Bandwidth efficient

### 4. Security Features
- ✅ File type validation
- ✅ Dangerous extension blocking
- ✅ Access control per file
- ✅ Public/private file support
- ✅ User-based permissions

### 5. Storage Features
- ✅ Local file system storage
- ✅ Organized directory structure
- ✅ File existence checking
- ✅ Automatic cleanup ready

## Configuration

### Application Properties

```properties
# File upload settings
app.file.upload.dir=uploads
app.file.thumbnail.dir=thumbnails
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB

# File size limits per category (bytes)
app.file.limits.image=5242880
app.file.limits.document=10485760
app.file.limits.video=104857600
```

### File Categories & Limits

| Category | Max Size | Allowed Types |
|----------|----------|---------------|
| IMAGE | 5MB | jpeg, png, gif, webp |
| DOCUMENT | 10MB | pdf, doc, docx |
| VIDEO | 100MB | mp4, avi, mov, wmv |
| AUDIO | 50MB | mp3, wav, ogg, m4a |
| SPREADSHEET | 10MB | xls, xlsx |
| PRESENTATION | 20MB | ppt, pptx |
| ARCHIVE | 50MB | zip, rar, 7z |

## Error Handling

### Validation Errors
```json
{
  "message": "File validation failed",
  "validationErrors": [
    "File size exceeds maximum allowed size of 10MB",
    "File type 'application/exe' is not allowed for category 'DOCUMENT'"
  ]
}
```

### Access Denied
```json
{
  "message": "Access denied to file: {fileId}"
}
```

### File Not Found
```json
{
  "message": "File not found: {fileId}"
}
```

## Mở rộng

### 1. Cloud Storage Support
Có thể dễ dàng thêm support cho AWS S3, Google Cloud Storage bằng cách implement `FileStoragePort`:

```java
@Component
public class S3FileStorageAdapter implements FileStoragePort {
    // Implementation for AWS S3
}
```

### 2. Database Integration
Cần implement `FileMetadataRepositoryPort` cho database persistence:

```java
@Repository
public class JpaFileMetadataRepository implements FileMetadataRepositoryPort {
    // JPA implementation
}
```

### 3. Additional Features
- CDN integration
- Image processing (resize, crop, filters)
- Video transcoding
- File versioning
- Bulk operations
- File sharing with expiration
- Download analytics

## Best Practices

1. **Security**: Luôn validate file type và size
2. **Performance**: Sử dụng streaming cho large files
3. **Storage**: Organize files by date và category
4. **Access Control**: Implement proper permission checking
5. **Monitoring**: Log file operations cho audit trail
6. **Cleanup**: Implement scheduled cleanup cho orphaned files

Framework này cung cấp foundation mạnh mẽ và có thể mở rộng cho tất cả file operations trong OpenSchool system.
