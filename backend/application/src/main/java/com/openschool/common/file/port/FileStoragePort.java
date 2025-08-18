package com.openschool.common.file.port;

import com.openschool.domain.common.file.FileData;
import com.openschool.domain.common.file.FileMetadata;

import java.io.InputStream;
import java.util.UUID;

/**
 * Port interface for file storage operations (Pure Java)
 */
public interface FileStoragePort {
    
    /**
     * Store file data and return stored file path
     */
    String storeFile(byte[] fileData, String fileName, String contentType);
    
    /**
     * Store file from input stream
     */
    String storeFile(InputStream inputStream, String fileName, String contentType, long contentLength);
    
    /**
     * Retrieve file data by file path
     */
    byte[] retrieveFile(String filePath);
    
    /**
     * Get input stream for file
     */
    InputStream getFileStream(String filePath);
    
    /**
     * Get partial file stream for range requests
     */
    InputStream getFileStream(String filePath, long start, long end);
    
    /**
     * Delete file by file path
     */
    boolean deleteFile(String filePath);
    
    /**
     * Check if file exists
     */
    boolean fileExists(String filePath);
    
    /**
     * Get file size
     */
    long getFileSize(String filePath);
    
    /**
     * Generate unique file name
     */
    String generateUniqueFileName(String originalFileName);
    
    /**
     * Generate thumbnail for image files
     */
    String generateThumbnail(String filePath, int width, int height);
}
