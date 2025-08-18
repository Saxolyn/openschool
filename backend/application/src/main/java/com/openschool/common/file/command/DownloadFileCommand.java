package com.openschool.common.file.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

/**
 * Command for file download operations (Pure Java)
 */
@Getter
@Builder
@AllArgsConstructor
public class DownloadFileCommand {
    
    private final UUID fileId;
    private final UUID requestedBy;
    private final boolean updateAccessTime;
    private final boolean includeMetadata;
    private final String accessToken;
    
    public DownloadFileCommand(UUID fileId, UUID requestedBy) {
        this.fileId = fileId;
        this.requestedBy = requestedBy;
        this.updateAccessTime = true;
        this.includeMetadata = true;
        this.accessToken = null;
    }
}
