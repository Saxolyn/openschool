package com.openschool.education.studentRecord.port.in.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudentRecordCommand {
    private UUID recordId;
    private String recordType;
    private String title;
    private String description;
    private String content;
    private LocalDate recordDate;
    private String category;
    private String severity;
    private String actionTaken;
    private String followUpRequired;
    private LocalDate followUpDate;
    private UUID updatedById;
    private String notes;
}
