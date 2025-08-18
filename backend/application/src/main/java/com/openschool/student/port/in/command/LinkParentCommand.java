package com.openschool.student.port.in.command;

import com.openschool.domain.student.GuardianRelationship;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LinkParentCommand {
    private UUID studentId;
    private UUID guardianId;
    private GuardianRelationship relationship;
}
