package com.openschool.education.exam.port.in.command;

import com.openschool.domain.exam.ExamType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateExamCommand {
    private String title;
    private String description;
    private String subject;
    private ExamType type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer durationMinutes;
    private UUID schoolId;
    private UUID academicYearId;
    private UUID gradeId;
    private UUID classId;
    private Integer totalMarks;
    private Integer passingMarks;
    private String instructions;
    private Integer maxStudents;
    private UUID supervisorId;
    private String venue;
    private String roomNumber;
}
