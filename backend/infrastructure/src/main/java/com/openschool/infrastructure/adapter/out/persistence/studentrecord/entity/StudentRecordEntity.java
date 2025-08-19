package com.openschool.infrastructure.adapter.out.persistence.studentrecord.entity;

import com.openschool.domain.studentrecord.StudentRecord;
import com.openschool.domain.studentrecord.StudentRecordStatus;
import com.openschool.infrastructure.adapter.out.persistence.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "student_records")
public class StudentRecordEntity extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(nullable = false)
    private UUID studentId;
    
    private String recordType;
    private String title;
    private String description;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    private LocalDate recordDate;
    private String category;
    private String severity;
    
    @Enumerated(EnumType.STRING)
    private StudentRecordStatus status;
    
    private String actionTaken;
    private String followUpRequired;
    private LocalDate followUpDate;
    private boolean isConfidential;
    
    private UUID schoolId;
    private UUID academicYearId;
    private UUID createdById;
    private UUID updatedById;
    private LocalDateTime updatedAt;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    public static StudentRecordEntity fromDomain(StudentRecord record) {
        if (record == null) return null;
        
        return StudentRecordEntity.builder()
                .id(record.getId())
                .studentId(record.getStudentId())
                .recordType(record.getRecordType())
                .title(record.getTitle())
                .description(record.getDescription())
                .content(record.getContent())
                .recordDate(record.getRecordDate())
                .category(record.getCategory())
                .severity(record.getSeverity())
                .status(record.getStatus())
                .actionTaken(record.getActionTaken())
                .followUpRequired(record.getFollowUpRequired())
                .followUpDate(record.getFollowUpDate())
                .isConfidential(record.isConfidential())
                .schoolId(record.getSchoolId())
                .academicYearId(record.getAcademicYearId())
                .createdById(record.getCreatedById())
                .updatedById(record.getUpdatedById())
                .updatedAt(record.getUpdatedAt())
                .notes(record.getNotes())
                .build();
    }
    
    public StudentRecord toDomain() {
        return StudentRecord.builder()
                .id(this.id)
                .studentId(this.studentId)
                .recordType(this.recordType)
                .title(this.title)
                .description(this.description)
                .content(this.content)
                .recordDate(this.recordDate)
                .category(this.category)
                .severity(this.severity)
                .status(this.status)
                .actionTaken(this.actionTaken)
                .followUpRequired(this.followUpRequired)
                .followUpDate(this.followUpDate)
                .isConfidential(this.isConfidential)
                .schoolId(this.schoolId)
                .academicYearId(this.academicYearId)
                .createdById(this.createdById)
                .updatedById(this.updatedById)
                .updatedAt(this.updatedAt)
                .notes(this.notes)
                .createdAt(this.getCreatedAt())
                .build();
    }
}
