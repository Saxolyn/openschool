package com.openschool.common.file.usecase;

import com.openschool.common.file.command.DownloadFileCommand;
import com.openschool.common.file.result.DownloadResult;
import com.openschool.domain.common.file.FileMetadata;

import java.util.UUID;

/**
 * Use case interface for file download operations (Pure Java)
 */
public interface DownloadFileUseCase {
    
    /**
     * Download file
     */
    DownloadResult downloadFile(DownloadFileCommand command);
    
    /**
     * Get file metadata only
     */
    FileMetadata getFileMetadata(UUID fileId);
    
    /**
     * Check if user can download file
     */
    boolean canDownload(UUID fileId, UUID userId);
}
