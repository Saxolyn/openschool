package com.openschool.administration.forum.port.in;

import com.openschool.administration.forum.port.in.command.CreateForumPostCommand;
import com.openschool.domain.forum.ForumPost;

public interface CreateForumPostUseCase {
    ForumPost createForumPost(CreateForumPostCommand command);
}
