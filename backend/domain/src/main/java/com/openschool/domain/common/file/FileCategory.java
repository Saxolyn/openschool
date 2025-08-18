package com.openschool.domain.common.file;

/**
 * Enum representing file categories
 */
public enum FileCategory {
    DOCUMENT("Document"),
    IMAGE("Image"),
    VIDEO("Video"),
    AUDIO("Audio"),
    SPREADSHEET("Spreadsheet"),
    PRESENTATION("Presentation"),
    ARCHIVE("Archive"),
    PROFILE_PHOTO("Profile Photo"),
    STUDENT_DOCUMENT("Student Document"),
    EMPLOYEE_DOCUMENT("Employee Document"),
    ACADEMIC_MATERIAL("Academic Material"),
    SYSTEM_FILE("System File"),
    OTHER("Other");
    
    private final String displayName;
    
    FileCategory(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
