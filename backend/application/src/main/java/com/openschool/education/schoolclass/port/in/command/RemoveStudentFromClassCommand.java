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
public class RemoveStudentFromClassCommand {
    
    private UUID studentId;
    private UUID classId;
    private LocalDate removalDate;
    private String removalReason;
    private String notes;
}
