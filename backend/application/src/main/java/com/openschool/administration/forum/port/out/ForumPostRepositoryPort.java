package com.openschool.administration.forum.port.out;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.forum.ForumPost;

import java.util.Optional;
import java.util.UUID;

public interface ForumPostRepositoryPort {
    
    ForumPost create(ForumPost post);
    
    ForumPost update(ForumPost post);
    
    Optional<ForumPost> findById(UUID postId);
    
    PageResult<ForumPost> findAll(PageInfo pageInfo);
    
    PageResult<ForumPost> findByThreadId(UUID threadId, PageInfo pageInfo);
    
    PageResult<ForumPost> findByAuthorId(UUID authorId, PageInfo pageInfo);
    
    boolean delete(UUID postId);
    
    long count();
    
    long countByThreadId(UUID threadId);
}
