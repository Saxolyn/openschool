package com.openschool.infrastructure.adapter.out.persistence.student.entity;

import com.openschool.domain.student.GuardianRelationship;
import com.openschool.infrastructure.adapter.out.persistence.common.BaseEntity;
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
@Table(name = "student_guardian", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "guardian_id"}))
public class StudentGuardianEntity extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "guardian_id", nullable = false)
    private GuardianEntity guardian;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GuardianRelationship relationship;
}
