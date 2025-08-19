package com.openschool.administration.forum.service;

import com.openschool.administration.forum.port.in.*;
import com.openschool.administration.forum.port.in.command.*;
import com.openschool.administration.forum.port.out.*;
import com.openschool.domain.forum.*;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class ForumService implements
        CreateForumThreadUseCase,
        CreateForumPostUseCase {

    private final ForumThreadRepositoryPort forumThreadRepository;
    private final ForumPostRepositoryPort forumPostRepository;

    @Override
    public ForumThread createForumThread(CreateForumThreadCommand command) {
        ForumThread thread = ForumThread.builder()
                .id(UUID.randomUUID())
                .title(command.getTitle())
                .content(command.getContent())
                .authorId(command.getAuthorId())
                .authorType(command.getAuthorType())
                .status(ForumThreadStatus.ACTIVE)
                .isPinned(false)
                .isLocked(false)
                .category(command.getCategory())
                .tags(command.getTags())
                .viewCount(0)
                .replyCount(0)
                .likeCount(0)
                .lastActivityDate(LocalDateTime.now())
                .schoolId(command.getSchoolId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return forumThreadRepository.create(thread);
    }

    @Override
    public ForumPost createForumPost(CreateForumPostCommand command) {
        // Verify thread exists
        ForumThread thread = forumThreadRepository.findById(command.getThreadId())
                .orElseThrow(() -> new RuntimeException("Thread not found"));

        if (!thread.canReply()) {
            throw new RuntimeException("Cannot reply to this thread");
        }

        ForumPost post = ForumPost.builder()
                .id(UUID.randomUUID())
                .threadId(command.getThreadId())
                .content(command.getContent())
                .authorId(command.getAuthorId())
                .authorType(command.getAuthorType())
                .status(ForumPostStatus.ACTIVE)
                .parentPostId(command.getParentPostId())
                .orderIndex(getNextOrderIndex(command.getThreadId()))
                .likeCount(0)
                .dislikeCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        ForumPost savedPost = forumPostRepository.create(post);

        // Update thread reply count and last activity
        thread.incrementReplyCount();
        thread.setLastPostId(savedPost.getId());
        thread.setLastPostAuthorId(savedPost.getAuthorId());
        forumThreadRepository.update(thread);

        return savedPost;
    }

    private Integer getNextOrderIndex(UUID threadId) {
        long postCount = forumPostRepository.countByThreadId(threadId);
        return (int) postCount + 1;
    }
}
