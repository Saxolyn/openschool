package com.openschool.education.grade.port.in.command;

import com.openschool.domain.grade.GradeLevel;
import com.openschool.domain.grade.GradeStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class CreateGradeCommand {
    private UUID schoolId;
    private String name;
    private String code;
    private GradeLevel level;

    private Integer minAge;
    private Integer maxAge;

    private Integer displayOrder;
    private boolean allowClass;

    private GradeStatus status;
}
