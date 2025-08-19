package com.openschool.domain.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Report {
    private UUID id;
    private String name;
    private String description;
    private ReportType type;
    private ReportCategory category;
    
    // Report configuration
    private Map<String, Object> parameters;
    private String templateId;
    private ReportFormat format;
    
    // Time period
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private String fiscalYear;
    
    // Generation details
    private ReportStatus status;
    private LocalDateTime generatedAt;
    private UUID generatedById;
    private String generationNotes;
    
    // Output details
    private String fileName;
    private String filePath;
    private String downloadUrl;
    private Long fileSize;
    
    // Access control
    private UUID schoolId;
    private UUID departmentId;
    private boolean isPublic;
    private String accessLevel;
    
    // Scheduling
    private boolean isScheduled;
    private String cronExpression;
    private LocalDateTime nextRunTime;
    
    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public boolean isGenerated() {
        return status == ReportStatus.COMPLETED;
    }
    
    public boolean isInProgress() {
        return status == ReportStatus.GENERATING;
    }
    
    public boolean hasFailed() {
        return status == ReportStatus.FAILED;
    }
    
    public void startGeneration(UUID generatedById) {
        this.status = ReportStatus.GENERATING;
        this.generatedById = generatedById;
        this.generatedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void completeGeneration(String fileName, String filePath, Long fileSize) {
        this.status = ReportStatus.COMPLETED;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void failGeneration(String errorMessage) {
        this.status = ReportStatus.FAILED;
        this.generationNotes = errorMessage;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isAccessibleBy(UUID userId, String userRole) {
        if (isPublic) {
            return true;
        }
        
        if (generatedById.equals(userId)) {
            return true;
        }
        
        // Additional access control logic based on role and access level
        return "ADMIN".equals(userRole) || "MANAGER".equals(userRole);
    }
}
