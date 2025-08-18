package com.openschool.education.schoolclass.port.in.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CreateClassCommand {
    private String name;
    private String code;
    private String description;
    private UUID schoolId;
    private UUID gradeId;
    private UUID academicYearId;
    private Integer maxStudents;
    private UUID homeroomTeacherId;
}
