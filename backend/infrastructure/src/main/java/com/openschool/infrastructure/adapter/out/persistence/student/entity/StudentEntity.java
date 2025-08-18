package com.openschool.infrastructure.adapter.out.persistence.student.entity;

import com.openschool.domain.student.Gender;
import com.openschool.domain.student.Student;
import com.openschool.domain.student.StudentStatus;
import com.openschool.infrastructure.adapter.out.persistence.common.BaseEntity;
import com.openschool.infrastructure.adapter.out.persistence.grade.entity.GradeEntity;
import com.openschool.infrastructure.adapter.out.persistence.school.entity.SchoolEntity;
import com.openschool.infrastructure.adapter.out.persistence.schoolclass.entity.SchoolClassEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "student")
public class StudentEntity extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(nullable = false, unique = true)
    private String studentCode;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(nullable = false)
    private LocalDate dateOfBirth;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;
    
    private String email;
    private String phoneNumber;
    private String address;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "school_id", nullable = false)
    private SchoolEntity school;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_grade_id")
    private GradeEntity currentGrade;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_class_id")
    private SchoolClassEntity currentClass;
    
    @Column(nullable = false)
    private LocalDate enrollmentDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudentStatus status;
    
    public static StudentEntity fromDomain(Student student, SchoolEntity school, GradeEntity grade, SchoolClassEntity schoolClass) {
        if (student == null || school == null) {
            return null;
        }
        return StudentEntity.builder()
                .id(student.getId())
                .studentCode(student.getStudentCode())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .dateOfBirth(student.getDateOfBirth())
                .gender(student.getGender())
                .email(student.getEmail())
                .phoneNumber(student.getPhoneNumber())
                .address(student.getAddress())
                .school(school)
                .currentGrade(grade)
                .currentClass(schoolClass)
                .enrollmentDate(student.getEnrollmentDate())
                .status(student.getStatus())
                .build();
    }
    
    public Student toDomain() {
        return Student.builder()
                .id(this.id)
                .studentCode(this.studentCode)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .dateOfBirth(this.dateOfBirth)
                .gender(this.gender)
                .email(this.email)
                .phoneNumber(this.phoneNumber)
                .address(this.address)
                .schoolId(this.school != null ? this.school.getId() : null)
                .currentGradeId(this.currentGrade != null ? this.currentGrade.getId() : null)
                .currentClassId(this.currentClass != null ? this.currentClass.getId() : null)
                .enrollmentDate(this.enrollmentDate)
                .status(this.status)
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }
}
