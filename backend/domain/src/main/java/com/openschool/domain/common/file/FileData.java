package com.openschool.domain.common.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Domain value object representing file data
 */
@Getter
@Builder
@AllArgsConstructor
public class FileData {
    
    private final FileMetadata metadata;
    private final byte[] content;
    
    public boolean hasContent() {
        return content != null && content.length > 0;
    }
    
    public long getContentSize() {
        return content != null ? content.length : 0;
    }
}
