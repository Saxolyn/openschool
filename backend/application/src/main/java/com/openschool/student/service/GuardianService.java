package com.openschool.student.service;

import com.openschool.domain.student.Guardian;
import com.openschool.student.exception.ExceptionMessage;
import com.openschool.student.exception.StudentException;
import com.openschool.student.port.in.command.CreateGuardianCommand;
import com.openschool.student.port.out.GuardianRepositoryPort;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class GuardianService {
    
    private final GuardianRepositoryPort guardianRepository;
    
    public Guardian createGuardian(CreateGuardianCommand command) {
        // Validate email uniqueness if provided
        if (command.getEmail() != null && guardianRepository.existsByEmail(command.getEmail())) {
            throw new StudentException(ExceptionMessage.GUARDIAN_EMAIL_ALREADY_EXISTS.getMessage());
        }
        
        // Validate phone uniqueness if provided
        if (command.getPhoneNumber() != null && guardianRepository.existsByPhoneNumber(command.getPhoneNumber())) {
            throw new StudentException(ExceptionMessage.GUARDIAN_PHONE_ALREADY_EXISTS.getMessage());
        }
        
        // Create guardian domain object
        Guardian guardian = Guardian.builder()
                .id(UUID.randomUUID())
                .firstName(command.getFirstName())
                .lastName(command.getLastName())
                .email(command.getEmail())
                .phoneNumber(command.getPhoneNumber())
                .address(command.getAddress())
                .occupation(command.getOccupation())
                .relationship(command.getRelationship())
                .emergencyPhoneNumber(command.getEmergencyPhoneNumber())
                .workPhoneNumber(command.getWorkPhoneNumber())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        return guardianRepository.create(guardian);
    }
}
