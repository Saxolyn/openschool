package com.openschool.common.file.service;

import com.openschool.common.file.command.DownloadFileCommand;
import com.openschool.common.file.command.StreamFileCommand;
import com.openschool.common.file.command.UploadFileCommand;
import com.openschool.common.file.exception.FileAccessDeniedException;
import com.openschool.common.file.exception.FileNotFoundException;
import com.openschool.common.file.port.FileMetadataRepositoryPort;
import com.openschool.common.file.port.FileStoragePort;
import com.openschool.common.file.result.DownloadResult;
import com.openschool.common.file.result.StreamResult;
import com.openschool.common.file.result.UploadResult;
import com.openschool.common.file.usecase.DownloadFileUseCase;
import com.openschool.common.file.usecase.StreamFileUseCase;
import com.openschool.common.file.usecase.UploadFileUseCase;
import com.openschool.common.file.validation.FileValidator;
import com.openschool.domain.common.file.FileCategory;
import com.openschool.domain.common.file.FileData;
import com.openschool.domain.common.file.FileMetadata;
import com.openschool.domain.common.file.FileStatus;
import lombok.AllArgsConstructor;

import java.io.InputStream;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Service implementation for file operations
 */
@AllArgsConstructor
public class FileService implements UploadFileUseCase, DownloadFileUseCase, StreamFileUseCase {
    
    private final FileStoragePort fileStoragePort;
    private final FileMetadataRepositoryPort fileMetadataRepository;
    
    @Override
    public UploadResult uploadFile(UploadFileCommand command) {
        long startTime = System.currentTimeMillis();
        
        try {
            // Validate file
            validateFile(command);
            
            // Generate unique file name
            String storedFileName = fileStoragePort.generateUniqueFileName(command.getOriginalFileName());
            
            // Store file
            String filePath = fileStoragePort.storeFile(
                command.getFileData(), 
                storedFileName, 
                command.getContentType()
            );
            
            // Calculate checksum
            String checksum = calculateChecksum(command.getFileData());
            
            // Get file extension
            String fileExtension = getFileExtension(command.getOriginalFileName());
            
            // Create file metadata
            FileMetadata metadata = FileMetadata.builder()
                    .id(UUID.randomUUID())
                    .originalFileName(command.getOriginalFileName())
                    .storedFileName(storedFileName)
                    .contentType(command.getContentType())
                    .fileSize(command.getFileData().length)
                    .filePath(filePath)
                    .fileExtension(fileExtension)
                    .checksum(checksum)
                    .category(command.getCategory())
                    .status(FileStatus.UPLOADED)
                    .uploadedBy(command.getUploadedBy())
                    .uploadedAt(LocalDateTime.now())
                    .lastAccessedAt(LocalDateTime.now())
                    .description(command.getDescription())
                    .isPublic(command.isPublic())
                    .build();
            
            // Save metadata
            FileMetadata savedMetadata = fileMetadataRepository.save(metadata);
            
            // Generate thumbnail if needed
            String thumbnailUrl = null;
            if (command.isGenerateThumbnail() && savedMetadata.isImage()) {
                try {
                    thumbnailUrl = fileStoragePort.generateThumbnail(filePath, 200, 200);
                } catch (Exception e) {
                    // Thumbnail generation failed, but upload is still successful
                }
            }
            
            long processingTime = System.currentTimeMillis() - startTime;
            
            return UploadResult.success(savedMetadata, null, thumbnailUrl, processingTime);
            
        } catch (Exception e) {
            return UploadResult.failure("Upload failed: " + e.getMessage());
        }
    }
    
    @Override
    public void validateFile(UploadFileCommand command) {
        FileValidator.validateUploadCommand(command);
    }
    
    @Override
    public long getMaxFileSize(String category) {
        try {
            FileCategory fileCategory = FileCategory.valueOf(category.toUpperCase());
            return FileValidator.getMaxFileSize(fileCategory);
        } catch (IllegalArgumentException e) {
            return 10 * 1024 * 1024; // Default 10MB
        }
    }
    
    @Override
    public String[] getAllowedFileTypes(String category) {
        try {
            FileCategory fileCategory = FileCategory.valueOf(category.toUpperCase());
            return FileValidator.getAllowedFileTypes(fileCategory);
        } catch (IllegalArgumentException e) {
            return new String[0];
        }
    }
    
    @Override
    public DownloadResult downloadFile(DownloadFileCommand command) {
        long startTime = System.currentTimeMillis();
        
        try {
            // Check access permission
            if (!canDownload(command.getFileId(), command.getRequestedBy())) {
                throw new FileAccessDeniedException("Access denied to file: " + command.getFileId());
            }
            
            // Get file metadata
            Optional<FileMetadata> metadataOpt = fileMetadataRepository.findById(command.getFileId());
            if (metadataOpt.isEmpty()) {
                throw new FileNotFoundException("File not found: " + command.getFileId());
            }
            
            FileMetadata metadata = metadataOpt.get();
            
            // Check file exists in storage
            if (!fileStoragePort.fileExists(metadata.getFilePath())) {
                throw new FileNotFoundException("File not found in storage: " + metadata.getFilePath());
            }
            
            // Retrieve file content
            byte[] fileContent = fileStoragePort.retrieveFile(metadata.getFilePath());
            
            // Update access time if requested
            if (command.isUpdateAccessTime()) {
                fileMetadataRepository.updateLastAccessedTime(command.getFileId(), LocalDateTime.now());
            }
            
            FileData fileData = FileData.builder()
                    .metadata(metadata)
                    .content(fileContent)
                    .build();
            
            long processingTime = System.currentTimeMillis() - startTime;
            
            return DownloadResult.success(fileData, processingTime);
            
        } catch (Exception e) {
            return DownloadResult.failure("Download failed: " + e.getMessage());
        }
    }
    
    @Override
    public FileMetadata getFileMetadata(UUID fileId) {
        Optional<FileMetadata> metadataOpt = fileMetadataRepository.findById(fileId);
        if (metadataOpt.isEmpty()) {
            throw new FileNotFoundException("File not found: " + fileId);
        }
        return metadataOpt.get();
    }
    
    @Override
    public boolean canDownload(UUID fileId, UUID userId) {
        Optional<FileMetadata> metadataOpt = fileMetadataRepository.findById(fileId);
        if (metadataOpt.isEmpty()) {
            return false;
        }
        
        FileMetadata metadata = metadataOpt.get();
        
        // Public files can be downloaded by anyone
        if (metadata.isPublic()) {
            return true;
        }
        
        // Owner can always download
        if (metadata.getUploadedBy().equals(userId)) {
            return true;
        }
        
        // Add more access control logic here based on business rules
        // For example: check if user has permission to access this file
        
        return false;
    }
    
    @Override
    public StreamResult streamFile(StreamFileCommand command) {
        try {
            // Check access permission
            if (!canStream(command.getFileId(), command.getRequestedBy())) {
                throw new FileAccessDeniedException("Access denied to file: " + command.getFileId());
            }
            
            // Get file metadata
            FileMetadata metadata = getFileMetadata(command.getFileId());
            
            // Get file stream
            InputStream inputStream;
            if (command.hasRange()) {
                inputStream = fileStoragePort.getFileStream(
                    metadata.getFilePath(), 
                    command.getRangeStart(), 
                    command.getRangeEnd()
                );
                
                long contentLength = command.getRangeEnd() - command.getRangeStart() + 1;
                return StreamResult.successPartial(
                    inputStream, metadata, contentLength, 
                    command.getRangeStart(), command.getRangeEnd()
                );
            } else {
                inputStream = fileStoragePort.getFileStream(metadata.getFilePath());
                return StreamResult.success(inputStream, metadata, metadata.getFileSize());
            }
            
        } catch (Exception e) {
            return StreamResult.failure("Stream failed: " + e.getMessage());
        }
    }
    
    @Override
    public StreamResult streamFileRange(StreamFileCommand command) {
        return streamFile(command);
    }
    
    @Override
    public boolean canStream(UUID fileId, UUID userId) {
        // Same logic as canDownload for now
        return canDownload(fileId, userId);
    }
    
    // Helper methods
    private String calculateChecksum(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(data);
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            return null;
        }
    }
    
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
