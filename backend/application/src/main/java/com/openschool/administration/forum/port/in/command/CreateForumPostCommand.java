package com.openschool.administration.forum.port.in.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateForumPostCommand {
    private UUID threadId;
    private String content;
    private UUID authorId;
    private String authorType;
    private UUID parentPostId;
}
