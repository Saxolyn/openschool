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
public class TransferStudentCommand {
    
    private UUID studentId;
    private UUID newSchoolId;
    private UUID newGradeId;
    private UUID newClassId;
    private LocalDate transferDate;
    private String transferReason;
    private String notes;
}
