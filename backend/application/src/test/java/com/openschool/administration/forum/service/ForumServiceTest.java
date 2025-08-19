package com.openschool.administration.forum.service;

import com.openschool.administration.forum.port.in.command.CreateForumPostCommand;
import com.openschool.administration.forum.port.in.command.CreateForumThreadCommand;
import com.openschool.administration.forum.port.out.ForumPostRepositoryPort;
import com.openschool.administration.forum.port.out.ForumThreadRepositoryPort;
import com.openschool.domain.forum.ForumPost;
import com.openschool.domain.forum.ForumPostStatus;
import com.openschool.domain.forum.ForumThread;
import com.openschool.domain.forum.ForumThreadStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ForumServiceTest {

    @Mock
    private ForumThreadRepositoryPort forumThreadRepository;

    @Mock
    private ForumPostRepositoryPort forumPostRepository;

    @InjectMocks
    private ForumService forumService;

    private CreateForumThreadCommand threadCommand;
    private CreateForumPostCommand postCommand;
    private ForumThread thread;

    @BeforeEach
    void setUp() {
        threadCommand = CreateForumThreadCommand.builder()
                .title("Test Thread")
                .content("This is a test thread")
                .authorId(UUID.randomUUID())
                .authorType("TEACHER")
                .category("GENERAL")
                .tags("test,forum")
                .schoolId(UUID.randomUUID())
                .build();

        thread = ForumThread.builder()
                .id(UUID.randomUUID())
                .title(threadCommand.getTitle())
                .content(threadCommand.getContent())
                .status(ForumThreadStatus.ACTIVE)
                .isPinned(false)
                .isLocked(false)
                .viewCount(0)
                .replyCount(0)
                .likeCount(0)
                .createdAt(LocalDateTime.now())
                .build();

        postCommand = CreateForumPostCommand.builder()
                .threadId(thread.getId())
                .content("This is a test post")
                .authorId(UUID.randomUUID())
                .authorType("STUDENT")
                .build();
    }

    @Test
    void createForumThread_Success() {
        // Given
        when(forumThreadRepository.create(any(ForumThread.class))).thenReturn(thread);

        // When
        ForumThread result = forumService.createForumThread(threadCommand);

        // Then
        assertNotNull(result);
        assertEquals("Test Thread", result.getTitle());
        assertEquals(ForumThreadStatus.ACTIVE, result.getStatus());
        assertFalse(result.isPinned());
        assertFalse(result.isLocked());
        assertEquals(0, result.getViewCount());
        assertEquals(0, result.getReplyCount());
        verify(forumThreadRepository).create(any(ForumThread.class));
    }

    @Test
    void createForumPost_Success() {
        // Given
        when(forumThreadRepository.findById(thread.getId())).thenReturn(Optional.of(thread));
        when(forumPostRepository.countByThreadId(thread.getId())).thenReturn(0L);
        
        ForumPost mockPost = ForumPost.builder()
                .id(UUID.randomUUID())
                .threadId(thread.getId())
                .content(postCommand.getContent())
                .status(ForumPostStatus.ACTIVE)
                .orderIndex(1)
                .build();
        
        when(forumPostRepository.create(any(ForumPost.class))).thenReturn(mockPost);
        when(forumThreadRepository.update(any(ForumThread.class))).thenReturn(thread);

        // When
        ForumPost result = forumService.createForumPost(postCommand);

        // Then
        assertNotNull(result);
        assertEquals(postCommand.getContent(), result.getContent());
        assertEquals(ForumPostStatus.ACTIVE, result.getStatus());
        assertEquals(1, result.getOrderIndex());
        verify(forumPostRepository).create(any(ForumPost.class));
        verify(forumThreadRepository).update(any(ForumThread.class));
    }

    @Test
    void createForumPost_ThreadNotFound_ThrowsException() {
        // Given
        when(forumThreadRepository.findById(thread.getId())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> forumService.createForumPost(postCommand));
    }
}
