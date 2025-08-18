package com.openschool.common.file.port;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.common.file.FileCategory;
import com.openschool.domain.common.file.FileMetadata;
import com.openschool.domain.common.file.FileStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port interface for file metadata repository operations (Pure Java)
 */
public interface FileMetadataRepositoryPort {
    
    /**
     * Save file metadata
     */
    FileMetadata save(FileMetadata fileMetadata);
    
    /**
     * Update file metadata
     */
    FileMetadata update(FileMetadata fileMetadata);
    
    /**
     * Find file metadata by ID
     */
    Optional<FileMetadata> findById(UUID fileId);
    
    /**
     * Find file metadata by stored file name
     */
    Optional<FileMetadata> findByStoredFileName(String storedFileName);
    
    /**
     * Find files by uploader
     */
    List<FileMetadata> findByUploadedBy(UUID uploadedBy);
    
    /**
     * Find files by category
     */
    PageResult<FileMetadata> findByCategory(FileCategory category, PageInfo pageInfo);
    
    /**
     * Find files by status
     */
    PageResult<FileMetadata> findByStatus(FileStatus status, PageInfo pageInfo);
    
    /**
     * Find public files
     */
    PageResult<FileMetadata> findPublicFiles(PageInfo pageInfo);
    
    /**
     * Search files by name or description
     */
    PageResult<FileMetadata> search(String searchTerm, PageInfo pageInfo);
    
    /**
     * Find files uploaded between dates
     */
    List<FileMetadata> findByUploadDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Update last accessed time
     */
    void updateLastAccessedTime(UUID fileId, LocalDateTime accessTime);
    
    /**
     * Update file status
     */
    void updateStatus(UUID fileId, FileStatus status);
    
    /**
     * Delete file metadata
     */
    boolean delete(UUID fileId);
    
    /**
     * Check if file exists
     */
    boolean existsById(UUID fileId);
    
    /**
     * Count files by category
     */
    long countByCategory(FileCategory category);
    
    /**
     * Count files by uploader
     */
    long countByUploadedBy(UUID uploadedBy);
    
    /**
     * Get total storage used by uploader
     */
    long getTotalStorageUsed(UUID uploadedBy);
}
