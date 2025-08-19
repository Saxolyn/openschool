package com.openschool.administration.forum.port.in;

import com.openschool.administration.forum.port.in.command.CreateForumThreadCommand;
import com.openschool.domain.forum.ForumThread;

public interface CreateForumThreadUseCase {
    ForumThread createForumThread(CreateForumThreadCommand command);
}
