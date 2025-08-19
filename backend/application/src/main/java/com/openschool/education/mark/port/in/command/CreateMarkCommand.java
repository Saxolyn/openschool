package com.openschool.education.mark.port.in.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateMarkCommand {
    private UUID studentId;
    private UUID subjectId;
    private UUID examId;
    private UUID classId;
    private String assessmentType;
    private String assessmentName;
    private Double obtainedMarks;
    private Double totalMarks;
    private UUID academicYearId;
    private UUID semesterId;
    private String term;
    private UUID enteredById;
    private String teacherComments;
    private String feedback;
}
