package com.openschool.education.student.port.in.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AssignStudentToClassCommand {
    
    private UUID studentId;
    private UUID classId;
    private LocalDate assignmentDate;
    private String notes;
}
