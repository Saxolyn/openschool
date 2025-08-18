package com.openschool.common.file.usecase;

import com.openschool.common.file.command.StreamFileCommand;
import com.openschool.common.file.result.StreamResult;

import java.util.UUID;

/**
 * Use case interface for file streaming operations (Pure Java)
 */
public interface StreamFileUseCase {
    
    /**
     * Stream file
     */
    StreamResult streamFile(StreamFileCommand command);
    
    /**
     * Stream file with range support
     */
    StreamResult streamFileRange(StreamFileCommand command);
    
    /**
     * Check if user can stream file
     */
    boolean canStream(UUID fileId, UUID userId);
}
