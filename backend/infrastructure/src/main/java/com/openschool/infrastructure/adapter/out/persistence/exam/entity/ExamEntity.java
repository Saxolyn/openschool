package com.openschool.infrastructure.adapter.out.persistence.exam.entity;

import com.openschool.domain.exam.Exam;
import com.openschool.domain.exam.ExamStatus;
import com.openschool.domain.exam.ExamType;
import com.openschool.infrastructure.adapter.out.persistence.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "exams")
public class ExamEntity extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(nullable = false)
    private String title;
    
    private String description;
    private String subject;
    
    @Enumerated(EnumType.STRING)
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
    
    @Enumerated(EnumType.STRING)
    private ExamStatus status;
    
    private String instructions;
    private Integer maxStudents;
    private Integer registeredStudents;
    private UUID supervisorId;
    private String venue;
    private String roomNumber;
    private boolean resultsPublished;
    private LocalDateTime resultsPublishedAt;
    
    public static ExamEntity fromDomain(Exam exam) {
        if (exam == null) return null;
        
        return ExamEntity.builder()
                .id(exam.getId())
                .title(exam.getTitle())
                .description(exam.getDescription())
                .subject(exam.getSubject())
                .type(exam.getType())
                .startTime(exam.getStartTime())
                .endTime(exam.getEndTime())
                .durationMinutes(exam.getDurationMinutes())
                .schoolId(exam.getSchoolId())
                .academicYearId(exam.getAcademicYearId())
                .gradeId(exam.getGradeId())
                .classId(exam.getClassId())
                .totalMarks(exam.getTotalMarks())
                .passingMarks(exam.getPassingMarks())
                .status(exam.getStatus())
                .instructions(exam.getInstructions())
                .maxStudents(exam.getMaxStudents())
                .registeredStudents(exam.getRegisteredStudents())
                .supervisorId(exam.getSupervisorId())
                .venue(exam.getVenue())
                .roomNumber(exam.getRoomNumber())
                .resultsPublished(exam.isResultsPublished())
                .resultsPublishedAt(exam.getResultsPublishedAt())
                .build();
    }
    
    public Exam toDomain() {
        return Exam.builder()
                .id(this.id)
                .title(this.title)
                .description(this.description)
                .subject(this.subject)
                .type(this.type)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .durationMinutes(this.durationMinutes)
                .schoolId(this.schoolId)
                .academicYearId(this.academicYearId)
                .gradeId(this.gradeId)
                .classId(this.classId)
                .totalMarks(this.totalMarks)
                .passingMarks(this.passingMarks)
                .status(this.status)
                .instructions(this.instructions)
                .maxStudents(this.maxStudents)
                .registeredStudents(this.registeredStudents)
                .supervisorId(this.supervisorId)
                .venue(this.venue)
                .roomNumber(this.roomNumber)
                .resultsPublished(this.resultsPublished)
                .resultsPublishedAt(this.resultsPublishedAt)
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }
}
