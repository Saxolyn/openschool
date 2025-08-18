package com.openschool.infrastructure.adapter.out.persistence.student.entity;

import com.openschool.domain.student.Guardian;
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
@Table(name = "guardian")
public class GuardianEntity extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    private String email;
    private String phoneNumber;
    private String address;
    private String occupation;
    
    @Enumerated(EnumType.STRING)
    private GuardianRelationship relationship;
    
    private String emergencyPhoneNumber;
    private String workPhoneNumber;
    
    public static GuardianEntity fromDomain(Guardian guardian) {
        if (guardian == null) {
            return null;
        }
        return GuardianEntity.builder()
                .id(guardian.getId())
                .firstName(guardian.getFirstName())
                .lastName(guardian.getLastName())
                .email(guardian.getEmail())
                .phoneNumber(guardian.getPhoneNumber())
                .address(guardian.getAddress())
                .occupation(guardian.getOccupation())
                .relationship(guardian.getRelationship())
                .emergencyPhoneNumber(guardian.getEmergencyPhoneNumber())
                .workPhoneNumber(guardian.getWorkPhoneNumber())
                .build();
    }
    
    public Guardian toDomain() {
        return Guardian.builder()
                .id(this.id)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .email(this.email)
                .phoneNumber(this.phoneNumber)
                .address(this.address)
                .occupation(this.occupation)
                .relationship(this.relationship)
                .emergencyPhoneNumber(this.emergencyPhoneNumber)
                .workPhoneNumber(this.workPhoneNumber)
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }
}
