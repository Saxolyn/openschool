package com.openschool.domain.common.file;

/**
 * Enum representing file status
 */
public enum FileStatus {
    UPLOADING("Uploading"),
    UPLOADED("Uploaded"),
    PROCESSING("Processing"),
    READY("Ready"),
    ERROR("Error"),
    DELETED("Deleted"),
    QUARANTINED("Quarantined");
    
    private final String displayName;
    
    FileStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
