package com.openschool.domain.teachingmaterial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TeachingMaterial {
    private UUID id;
    private String title;
    private String description;
    private String subject;
    private MaterialType type;
    
    // Content
    private String content;
    private String fileUrl;
    private String fileName;
    private Long fileSize;
    private String mimeType;
    
    // Academic context
    private UUID schoolId;
    private UUID gradeId;
    private UUID classId;
    private UUID teacherId;
    
    // Access control
    private MaterialVisibility visibility;
    private boolean isPublic;
    private String accessCode;
    
    // Usage tracking
    private Integer downloadCount;
    private Integer viewCount;
    private LocalDateTime lastAccessedAt;
    
    // Status
    private MaterialStatus status;
    private boolean isApproved;
    private UUID approvedById;
    private LocalDateTime approvedAt;
    
    // Tags and categories
    private String tags;
    private String category;
    private String difficulty;
    
    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public boolean isAccessible() {
        return status == MaterialStatus.PUBLISHED && isApproved;
    }
    
    public void incrementViewCount() {
        this.viewCount++;
        this.lastAccessedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void incrementDownloadCount() {
        this.downloadCount++;
        this.lastAccessedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void approve(UUID approvedById) {
        this.isApproved = true;
        this.approvedById = approvedById;
        this.approvedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void publish() {
        this.status = MaterialStatus.PUBLISHED;
        this.updatedAt = LocalDateTime.now();
    }
}
