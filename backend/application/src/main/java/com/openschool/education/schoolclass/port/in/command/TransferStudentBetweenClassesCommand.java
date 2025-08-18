package com.openschool.education.schoolclass.port.in.command;

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
public class TransferStudentBetweenClassesCommand {
    
    private UUID studentId;
    private UUID fromClassId;
    private UUID toClassId;
    private LocalDate transferDate;
    private String transferReason;
    private String notes;
}
