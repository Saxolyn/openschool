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
public class CreateForumThreadCommand {
    private String title;
    private String content;
    private UUID authorId;
    private String authorType;
    private String category;
    private String tags;
    private UUID schoolId;
}
