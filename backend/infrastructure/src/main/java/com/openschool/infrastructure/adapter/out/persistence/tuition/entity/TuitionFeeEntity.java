package com.openschool.infrastructure.adapter.out.persistence.tuition.entity;

import com.openschool.domain.tuition.TuitionFee;
import com.openschool.domain.tuition.TuitionFeeStatus;
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
@Table(name = "tuition_fees")
public class TuitionFeeEntity extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(nullable = false)
    private UUID studentId;
    
    private UUID academicYearId;
    private UUID gradeId;
    
    private String feeType;
    private String description;
    private Double baseAmount;
    private Double discountAmount;
    private Double scholarshipAmount;
    private Double finalAmount;
    private Double paidAmount;
    private Double outstandingAmount;
    
    @Enumerated(EnumType.STRING)
    private TuitionFeeStatus status;
    
    private LocalDateTime dueDate;
    private LocalDateTime lastPaymentDate;
    private UUID schoolId;
    
    public static TuitionFeeEntity fromDomain(TuitionFee tuitionFee) {
        if (tuitionFee == null) return null;
        
        return TuitionFeeEntity.builder()
                .id(tuitionFee.getId())
                .studentId(tuitionFee.getStudentId())
                .academicYearId(tuitionFee.getAcademicYearId())
                .gradeId(tuitionFee.getGradeId())
                .feeType(tuitionFee.getFeeType())
                .description(tuitionFee.getDescription())
                .baseAmount(tuitionFee.getBaseAmount())
                .discountAmount(tuitionFee.getDiscountAmount())
                .scholarshipAmount(tuitionFee.getScholarshipAmount())
                .finalAmount(tuitionFee.getFinalAmount())
                .paidAmount(tuitionFee.getPaidAmount())
                .outstandingAmount(tuitionFee.getOutstandingAmount())
                .status(tuitionFee.getStatus())
                .dueDate(tuitionFee.getDueDate())
                .lastPaymentDate(tuitionFee.getLastPaymentDate())
                .schoolId(tuitionFee.getSchoolId())
                .build();
    }
    
    public TuitionFee toDomain() {
        return TuitionFee.builder()
                .id(this.id)
                .studentId(this.studentId)
                .academicYearId(this.academicYearId)
                .gradeId(this.gradeId)
                .feeType(this.feeType)
                .description(this.description)
                .baseAmount(this.baseAmount)
                .discountAmount(this.discountAmount)
                .scholarshipAmount(this.scholarshipAmount)
                .finalAmount(this.finalAmount)
                .paidAmount(this.paidAmount)
                .outstandingAmount(this.outstandingAmount)
                .status(this.status)
                .dueDate(this.dueDate)
                .lastPaymentDate(this.lastPaymentDate)
                .schoolId(this.schoolId)
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }
}
