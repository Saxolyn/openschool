package com.openschool.domain.forum;

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
public class ForumPost {
    private UUID id;
    private UUID threadId;
    private String content;
    private UUID authorId;
    private String authorType; // STUDENT, EMPLOYEE, ADMIN
    
    // Post management
    private ForumPostStatus status;
    private UUID parentPostId; // For replies
    private Integer orderIndex;
    
    // Statistics
    private Integer likeCount;
    private Integer dislikeCount;
    
    // Moderation
    private UUID moderatorId;
    private String moderationNotes;
    private LocalDateTime moderatedAt;
    private String reportReason;
    private UUID reportedById;
    private LocalDateTime reportedAt;
    
    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public boolean isActive() {
        return status == ForumPostStatus.ACTIVE;
    }
    
    public boolean isReply() {
        return parentPostId != null;
    }
    
    public void like() {
        this.likeCount++;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void unlike() {
        if (likeCount > 0) {
            this.likeCount--;
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void report(UUID reportedById, String reason) {
        this.status = ForumPostStatus.REPORTED;
        this.reportedById = reportedById;
        this.reportReason = reason;
        this.reportedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void moderate(UUID moderatorId, String notes, ForumPostStatus newStatus) {
        this.moderatorId = moderatorId;
        this.moderationNotes = notes;
        this.moderatedAt = LocalDateTime.now();
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }
}
