package com.openschool.education.student.service;

import com.openschool.domain.student.Guardian;
import com.openschool.education.student.exception.ExceptionMessage;
import com.openschool.education.student.exception.StudentException;
import com.openschool.education.student.port.in.CreateGuardianUseCase;
import com.openschool.education.student.port.in.SearchGuardianUseCase;
import com.openschool.education.student.port.in.UpdateGuardianUseCase;
import com.openschool.student.port.in.*;
import com.openschool.education.student.port.in.command.CreateGuardianCommand;
import com.openschool.education.student.port.in.command.UpdateGuardianCommand;
import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.common.exception.DataNotFound;

import java.util.Optional;
import com.openschool.education.student.port.out.GuardianRepositoryPort;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class GuardianService implements
        CreateGuardianUseCase,
        UpdateGuardianUseCase,
        SearchGuardianUseCase {
    
    private final GuardianRepositoryPort guardianRepository;

    @Override
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

    @Override
    public Guardian updateGuardian(UpdateGuardianCommand command) {
        Optional<Guardian> guardianOpt = guardianRepository.findById(command.getGuardianId());
        if (guardianOpt.isEmpty()) {
            throw new DataNotFound("Guardian not found with id: " + command.getGuardianId());
        }

        Guardian guardian = guardianOpt.get();

        // Update fields if provided
        if (command.getFirstName() != null) {
            guardian.setFirstName(command.getFirstName());
        }
        if (command.getLastName() != null) {
            guardian.setLastName(command.getLastName());
        }
        if (command.getEmail() != null) {
            // Validate email uniqueness if changed
            if (!command.getEmail().equals(guardian.getEmail()) &&
                guardianRepository.existsByEmail(command.getEmail())) {
                throw new StudentException(ExceptionMessage.GUARDIAN_EMAIL_ALREADY_EXISTS.getMessage());
            }
            guardian.setEmail(command.getEmail());
        }
        if (command.getPhoneNumber() != null) {
            // Validate phone uniqueness if changed
            if (!command.getPhoneNumber().equals(guardian.getPhoneNumber()) &&
                guardianRepository.existsByPhoneNumber(command.getPhoneNumber())) {
                throw new StudentException(ExceptionMessage.GUARDIAN_PHONE_ALREADY_EXISTS.getMessage());
            }
            guardian.setPhoneNumber(command.getPhoneNumber());
        }
        if (command.getAddress() != null) {
            guardian.setAddress(command.getAddress());
        }
        if (command.getOccupation() != null) {
            guardian.setOccupation(command.getOccupation());
        }
        if (command.getRelationship() != null) {
            guardian.setRelationship(command.getRelationship());
        }
        if (command.getEmergencyPhoneNumber() != null) {
            guardian.setEmergencyPhoneNumber(command.getEmergencyPhoneNumber());
        }
        if (command.getWorkPhoneNumber() != null) {
            guardian.setWorkPhoneNumber(command.getWorkPhoneNumber());
        }

        guardian.setUpdatedAt(LocalDateTime.now());
        return guardianRepository.update(guardian);
    }

    @Override
    public PageResult<Guardian> searchGuardian(String searchTerm, PageInfo pageInfo) {
        return guardianRepository.search(searchTerm, pageInfo);
    }
}
