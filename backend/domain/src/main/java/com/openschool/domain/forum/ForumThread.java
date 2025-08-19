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
public class ForumThread {
    private UUID id;
    private String title;
    private String content;
    private UUID authorId;
    private String authorType; // STUDENT, EMPLOYEE, ADMIN
    
    // Thread management
    private ForumThreadStatus status;
    private boolean isPinned;
    private boolean isLocked;
    private String category;
    private String tags;
    
    // Statistics
    private Integer viewCount;
    private Integer replyCount;
    private Integer likeCount;
    
    // Last activity
    private UUID lastPostId;
    private UUID lastPostAuthorId;
    private LocalDateTime lastActivityDate;
    
    // Moderation
    private UUID moderatorId;
    private String moderationNotes;
    private LocalDateTime moderatedAt;
    
    // School assignment
    private UUID schoolId;
    
    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public boolean isActive() {
        return status == ForumThreadStatus.ACTIVE;
    }
    
    public boolean canReply() {
        return isActive() && !isLocked;
    }
    
    public void incrementViewCount() {
        this.viewCount++;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void incrementReplyCount() {
        this.replyCount++;
        this.lastActivityDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void pin() {
        this.isPinned = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void unpin() {
        this.isPinned = false;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void lock() {
        this.isLocked = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void unlock() {
        this.isLocked = false;
        this.updatedAt = LocalDateTime.now();
    }
}
