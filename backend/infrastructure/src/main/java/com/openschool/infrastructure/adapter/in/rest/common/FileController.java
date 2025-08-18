package com.openschool.infrastructure.adapter.in.rest.common;

import com.openschool.common.file.command.DownloadFileCommand;
import com.openschool.common.file.command.StreamFileCommand;
import com.openschool.common.file.command.UploadFileCommand;
import com.openschool.common.file.result.DownloadResult;
import com.openschool.common.file.result.StreamResult;
import com.openschool.common.file.result.UploadResult;
import com.openschool.common.file.usecase.DownloadFileUseCase;
import com.openschool.common.file.usecase.StreamFileUseCase;
import com.openschool.common.file.usecase.UploadFileUseCase;
import com.openschool.domain.common.file.FileCategory;
import com.openschool.domain.common.file.FileMetadata;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * REST Controller for file operations (Framework layer)
 */
@RestController
@RequestMapping("/api/files")
public class FileController {
    
    private final UploadFileUseCase uploadFileUseCase;
    private final DownloadFileUseCase downloadFileUseCase;
    private final StreamFileUseCase streamFileUseCase;
    
    public FileController(UploadFileUseCase uploadFileUseCase,
                         DownloadFileUseCase downloadFileUseCase,
                         StreamFileUseCase streamFileUseCase) {
        this.uploadFileUseCase = uploadFileUseCase;
        this.downloadFileUseCase = downloadFileUseCase;
        this.streamFileUseCase = streamFileUseCase;
    }
    
    @PostMapping("/upload")
    public ResponseEntity<UploadResult> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("category") String category,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "isPublic", defaultValue = "false") boolean isPublic,
            @RequestParam(value = "generateThumbnail", defaultValue = "false") boolean generateThumbnail) throws IOException {
        
        // Convert MultipartFile to byte[] (Framework → Application boundary)
        byte[] fileData = file.getBytes();
        String originalFileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        
        // For demo purposes, using a fixed user ID. In real app, get from security context
        UUID uploadedBy = UUID.randomUUID();
        
        FileCategory fileCategory;
        try {
            fileCategory = FileCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            fileCategory = FileCategory.OTHER;
        }
        
        UploadFileCommand command = UploadFileCommand.builder()
                .fileData(fileData)
                .originalFileName(originalFileName)
                .contentType(contentType)
                .category(fileCategory)
                .uploadedBy(uploadedBy)
                .description(description)
                .isPublic(isPublic)
                .generateThumbnail(generateThumbnail)
                .build();
        
        UploadResult result = uploadFileUseCase.uploadFile(command);
        
        if (result.isSuccessful()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
    
    @GetMapping("/{fileId}/download")
    public ResponseEntity<byte[]> downloadFile(
            @PathVariable UUID fileId,
            @RequestParam(value = "userId", required = false) UUID userId) {
        
        // For demo purposes, using provided userId or random. In real app, get from security context
        UUID requestedBy = userId != null ? userId : UUID.randomUUID();
        
        DownloadFileCommand command = new DownloadFileCommand(fileId, requestedBy);
        DownloadResult result = downloadFileUseCase.downloadFile(command);
        
        if (!result.isSuccessful()) {
            return ResponseEntity.notFound().build();
        }
        
        FileMetadata metadata = result.getMetadata();
        byte[] content = result.getContent();
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                       "attachment; filename=\"" + metadata.getOriginalFileName() + "\"")
                .contentType(MediaType.parseMediaType(metadata.getContentType()))
                .contentLength(content.length)
                .body(content);
    }
    
    @GetMapping("/{fileId}/stream")
    public ResponseEntity<InputStreamResource> streamFile(
            @PathVariable UUID fileId,
            @RequestParam(value = "userId", required = false) UUID userId,
            @RequestHeader(value = "Range", required = false) String rangeHeader) {
        
        // For demo purposes, using provided userId or random. In real app, get from security context
        UUID requestedBy = userId != null ? userId : UUID.randomUUID();
        
        StreamFileCommand.StreamFileCommandBuilder commandBuilder = StreamFileCommand.builder()
                .fileId(fileId)
                .requestedBy(requestedBy);
        
        // Parse Range header if present
        if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
            String range = rangeHeader.substring(6);
            String[] parts = range.split("-");
            if (parts.length > 0) {
                try {
                    long start = Long.parseLong(parts[0]);
                    commandBuilder.rangeStart(start);
                    
                    if (parts.length > 1 && !parts[1].isEmpty()) {
                        long end = Long.parseLong(parts[1]);
                        commandBuilder.rangeEnd(end);
                    }
                } catch (NumberFormatException e) {
                    // Invalid range, ignore
                }
            }
        }
        
        StreamFileCommand command = commandBuilder.build();
        StreamResult result = streamFileUseCase.streamFile(command);
        
        if (!result.isSuccessful()) {
            return ResponseEntity.notFound().build();
        }
        
        FileMetadata metadata = result.getFileMetadata();
        InputStreamResource resource = new InputStreamResource(result.getInputStream());
        
        ResponseEntity.BodyBuilder responseBuilder;
        
        if (result.isPartialContent()) {
            responseBuilder = ResponseEntity.status(HttpStatus.PARTIAL_CONTENT);
            responseBuilder.header(HttpHeaders.CONTENT_RANGE, result.getContentRange());
        } else {
            responseBuilder = ResponseEntity.ok();
        }
        
        return responseBuilder
                .contentType(MediaType.parseMediaType(metadata.getContentType()))
                .contentLength(result.getContentLength())
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .body(resource);
    }
    
    @GetMapping("/{fileId}/metadata")
    public ResponseEntity<FileMetadata> getFileMetadata(@PathVariable UUID fileId) {
        try {
            FileMetadata metadata = downloadFileUseCase.getFileMetadata(fileId);
            return ResponseEntity.ok(metadata);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/config/{category}")
    public ResponseEntity<FileConfigResponse> getFileConfig(@PathVariable String category) {
        long maxFileSize = uploadFileUseCase.getMaxFileSize(category);
        String[] allowedTypes = uploadFileUseCase.getAllowedFileTypes(category);
        
        FileConfigResponse response = new FileConfigResponse(maxFileSize, allowedTypes);
        return ResponseEntity.ok(response);
    }
    
    // Response class for file configuration
    public static class FileConfigResponse {
        private long maxFileSize;
        private String[] allowedFileTypes;
        
        public FileConfigResponse(long maxFileSize, String[] allowedFileTypes) {
            this.maxFileSize = maxFileSize;
            this.allowedFileTypes = allowedFileTypes;
        }
        
        public long getMaxFileSize() { return maxFileSize; }
        public String[] getAllowedFileTypes() { return allowedFileTypes; }
    }
}
