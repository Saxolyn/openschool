package com.openschool.common.file.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

/**
 * Command for file streaming operations (Pure Java)
 */
@Getter
@Builder
@AllArgsConstructor
public class StreamFileCommand {
    
    private final UUID fileId;
    private final UUID requestedBy;
    private final long rangeStart;
    private final long rangeEnd;
    private final boolean supportPartialContent;
    private final String accessToken;
    
    public StreamFileCommand(UUID fileId, UUID requestedBy) {
        this.fileId = fileId;
        this.requestedBy = requestedBy;
        this.rangeStart = 0;
        this.rangeEnd = -1; // -1 means end of file
        this.supportPartialContent = true;
        this.accessToken = null;
    }
    
    public boolean hasRange() {
        return rangeStart > 0 || rangeEnd > 0;
    }
}
