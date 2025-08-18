package com.openschool.infrastructure.adapter.out.persistence.schoolclass.entity;

import com.openschool.domain.schoolclass.model.ClassStatus;
import com.openschool.domain.schoolclass.model.SchoolClass;
import com.openschool.infrastructure.adapter.out.persistence.academic.entity.AcademicYearEntity;
import com.openschool.infrastructure.adapter.out.persistence.common.BaseEntity;
import com.openschool.infrastructure.adapter.out.persistence.employee.entity.EmployeeEntity;
import com.openschool.infrastructure.adapter.out.persistence.grade.entity.GradeEntity;
import com.openschool.infrastructure.adapter.out.persistence.school.entity.SchoolEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "school_class")
public class SchoolClassEntity extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String code;
    
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "school_id", nullable = false)
    private SchoolEntity school;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grade_id", nullable = false)
    private GradeEntity grade;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "academic_year_id", nullable = false)
    private AcademicYearEntity academicYear;
    
    private Integer maxStudents;
    
    @Column(nullable = false)
    private Integer currentStudentCount = 0;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homeroom_teacher_id")
    private EmployeeEntity homeroomTeacher;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClassStatus status;
    
    public static SchoolClassEntity fromDomain(SchoolClass schoolClass, SchoolEntity school, 
                                               GradeEntity grade, AcademicYearEntity academicYear, 
                                               EmployeeEntity homeroomTeacher) {
        if (schoolClass == null || school == null || grade == null || academicYear == null) {
            return null;
        }
        return SchoolClassEntity.builder()
                .id(schoolClass.getId())
                .name(schoolClass.getName())
                .code(schoolClass.getCode())
                .description(schoolClass.getDescription())
                .school(school)
                .grade(grade)
                .academicYear(academicYear)
                .maxStudents(schoolClass.getMaxStudents())
                .currentStudentCount(schoolClass.getCurrentStudentCount())
                .homeroomTeacher(homeroomTeacher)
                .status(schoolClass.getStatus())
                .build();
    }
    
    public SchoolClass toDomain() {
        return SchoolClass.builder()
                .id(this.id)
                .name(this.name)
                .code(this.code)
                .description(this.description)
                .schoolId(this.school != null ? this.school.getId() : null)
                .gradeId(this.grade != null ? this.grade.getId() : null)
                .academicYearId(this.academicYear != null ? this.academicYear.getId() : null)
                .maxStudents(this.maxStudents)
                .currentStudentCount(this.currentStudentCount)
                .homeroomTeacherId(this.homeroomTeacher != null ? this.homeroomTeacher.getEmployeeId() : null)
                .status(this.status)
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }

    public static SchoolClassEntity referenceOnly(UUID id) {
        SchoolClassEntity entity = new SchoolClassEntity();
        entity.setId(id);
        return entity;
    }
}
