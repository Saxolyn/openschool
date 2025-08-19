package com.openschool.infrastructure.adapter.out.persistence.forum.entity;

import com.openschool.domain.forum.ForumThread;
import com.openschool.domain.forum.ForumThreadStatus;
import com.openschool.infrastructure.adapter.out.persistence.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "forum_threads")
public class ForumThreadEntity extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    private UUID authorId;
    private String authorType;
    
    @Enumerated(EnumType.STRING)
    private ForumThreadStatus status;
    
    private boolean isPinned;
    private boolean isLocked;
    private String category;
    private String tags;
    
    private Integer viewCount;
    private Integer replyCount;
    private Integer likeCount;
    
    private UUID lastPostId;
    private UUID lastPostAuthorId;
    private LocalDateTime lastActivityDate;
    
    private UUID moderatorId;
    private String moderationNotes;
    private LocalDateTime moderatedAt;
    
    private UUID schoolId;
    
    public static ForumThreadEntity fromDomain(ForumThread thread) {
        if (thread == null) return null;
        
        return ForumThreadEntity.builder()
                .id(thread.getId())
                .title(thread.getTitle())
                .content(thread.getContent())
                .authorId(thread.getAuthorId())
                .authorType(thread.getAuthorType())
                .status(thread.getStatus())
                .isPinned(thread.isPinned())
                .isLocked(thread.isLocked())
                .category(thread.getCategory())
                .tags(thread.getTags())
                .viewCount(thread.getViewCount())
                .replyCount(thread.getReplyCount())
                .likeCount(thread.getLikeCount())
                .lastPostId(thread.getLastPostId())
                .lastPostAuthorId(thread.getLastPostAuthorId())
                .lastActivityDate(thread.getLastActivityDate())
                .moderatorId(thread.getModeratorId())
                .moderationNotes(thread.getModerationNotes())
                .moderatedAt(thread.getModeratedAt())
                .schoolId(thread.getSchoolId())
                .build();
    }
    
    public ForumThread toDomain() {
        return ForumThread.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .authorId(this.authorId)
                .authorType(this.authorType)
                .status(this.status)
                .isPinned(this.isPinned)
                .isLocked(this.isLocked)
                .category(this.category)
                .tags(this.tags)
                .viewCount(this.viewCount)
                .replyCount(this.replyCount)
                .likeCount(this.likeCount)
                .lastPostId(this.lastPostId)
                .lastPostAuthorId(this.lastPostAuthorId)
                .lastActivityDate(this.lastActivityDate)
                .moderatorId(this.moderatorId)
                .moderationNotes(this.moderationNotes)
                .moderatedAt(this.moderatedAt)
                .schoolId(this.schoolId)
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }
}
