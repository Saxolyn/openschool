package com.openschool.administration.forum.port.out;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.forum.ForumThread;

import java.util.Optional;
import java.util.UUID;

public interface ForumThreadRepositoryPort {
    
    ForumThread create(ForumThread thread);
    
    ForumThread update(ForumThread thread);
    
    Optional<ForumThread> findById(UUID threadId);
    
    PageResult<ForumThread> findAll(PageInfo pageInfo);
    
    PageResult<ForumThread> findBySchoolId(UUID schoolId, PageInfo pageInfo);
    
    PageResult<ForumThread> findByCategory(String category, PageInfo pageInfo);
    
    PageResult<ForumThread> findByAuthorId(UUID authorId, PageInfo pageInfo);
    
    boolean delete(UUID threadId);
    
    long count();
}
