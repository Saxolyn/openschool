package com.openschool.administration.equipment.service;

import com.openschool.administration.equipment.exception.EquipmentException;
import com.openschool.administration.equipment.port.in.*;
import com.openschool.administration.equipment.port.in.command.*;
import com.openschool.administration.equipment.port.out.EquipmentLoanRepositoryPort;
import com.openschool.administration.equipment.port.out.EquipmentMaintenanceRepositoryPort;
import com.openschool.administration.equipment.port.out.EquipmentRepositoryPort;
import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.equipment.*;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class EquipmentService implements
        CreateEquipmentUseCase,
        UpdateEquipmentDetailsUseCase,
        DeleteEquipmentUseCase,
        ViewEquipmentDetailsUseCase,
        GetListEquipmentUseCase,
        CheckEquipmentInventoryUseCase,
        AddEquipmentToInventoryUseCase,
        RemoveEquipmentFromInventoryUseCase,
        BorrowEquipmentUseCase,
        ReturnEquipmentUseCase,
        TrackEquipmentLoanUseCase,
        ScheduleMaintenanceUseCase,
        RecordRepairDetailsUseCase {

    private final EquipmentRepositoryPort equipmentRepository;
    private final EquipmentLoanRepositoryPort equipmentLoanRepository;
    private final EquipmentMaintenanceRepositoryPort equipmentMaintenanceRepository;

    @Override
    public Equipment createEquipment(CreateEquipmentCommand command) {
        // Validate unique code
        if (equipmentRepository.existsByCode(command.getCode())) {
            throw new EquipmentException("Equipment with code " + command.getCode() + " already exists");
        }
        
        // Validate unique serial number if provided
        if (command.getSerialNumber() != null && 
            equipmentRepository.existsBySerialNumber(command.getSerialNumber())) {
            throw new EquipmentException("Equipment with serial number " + command.getSerialNumber() + " already exists");
        }

        Equipment equipment = Equipment.builder()
                .id(UUID.randomUUID())
                .name(command.getName())
                .code(command.getCode())
                .description(command.getDescription())
                .brand(command.getBrand())
                .model(command.getModel())
                .serialNumber(command.getSerialNumber())
                .schoolId(command.getSchoolId())
                .departmentId(command.getDepartmentId())
                .location(command.getLocation())
                .type(command.getType())
                .status(command.getStatus() != null ? command.getStatus() : EquipmentStatus.ACTIVE)
                .condition(command.getCondition() != null ? command.getCondition() : EquipmentCondition.GOOD)
                .purchasePrice(command.getPurchasePrice())
                .purchaseDate(command.getPurchaseDate())
                .supplier(command.getSupplier())
                .warrantyInfo(command.getWarrantyInfo())
                .warrantyExpiry(command.getWarrantyExpiry())
                .quantity(command.getQuantity() != null ? command.getQuantity() : 1)
                .availableQuantity(command.getQuantity() != null ? command.getQuantity() : 1)
                .borrowedQuantity(0)
                .maintenanceQuantity(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return equipmentRepository.create(equipment);
    }

    @Override
    public Equipment updateEquipmentDetails(UpdateEquipmentCommand command) {
        Equipment existingEquipment = equipmentRepository.findById(command.getId())
                .orElseThrow(() -> new EquipmentException("Equipment not found with id: " + command.getId()));

        // Validate unique code if changed
        if (!existingEquipment.getCode().equals(command.getCode()) && 
            equipmentRepository.existsByCode(command.getCode())) {
            throw new EquipmentException("Equipment with code " + command.getCode() + " already exists");
        }

        Equipment updatedEquipment = Equipment.builder()
                .id(existingEquipment.getId())
                .name(command.getName())
                .code(command.getCode())
                .description(command.getDescription())
                .brand(command.getBrand())
                .model(command.getModel())
                .serialNumber(command.getSerialNumber())
                .schoolId(command.getSchoolId())
                .departmentId(command.getDepartmentId())
                .location(command.getLocation())
                .assignedToUserId(command.getAssignedToUserId())
                .type(command.getType())
                .status(command.getStatus())
                .condition(command.getCondition())
                .purchasePrice(command.getPurchasePrice())
                .purchaseDate(command.getPurchaseDate())
                .supplier(command.getSupplier())
                .warrantyInfo(command.getWarrantyInfo())
                .warrantyExpiry(command.getWarrantyExpiry())
                .nextMaintenanceDate(command.getNextMaintenanceDate())
                .maintenanceNotes(command.getMaintenanceNotes())
                // Keep existing inventory data
                .quantity(existingEquipment.getQuantity())
                .availableQuantity(existingEquipment.getAvailableQuantity())
                .borrowedQuantity(existingEquipment.getBorrowedQuantity())
                .maintenanceQuantity(existingEquipment.getMaintenanceQuantity())
                .lastMaintenanceDate(existingEquipment.getLastMaintenanceDate())
                .createdAt(existingEquipment.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        return equipmentRepository.update(updatedEquipment);
    }

    @Override
    public void deleteEquipment(UUID equipmentId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new EquipmentException("Equipment not found with id: " + equipmentId));

        // Check if equipment has active loans
        long activeLoans = equipmentLoanRepository.countByEquipmentId(equipmentId);
        if (activeLoans > 0) {
            throw new EquipmentException("Cannot delete equipment with active loans");
        }

        boolean deleted = equipmentRepository.delete(equipmentId);
        if (!deleted) {
            throw new EquipmentException("Failed to delete equipment");
        }
    }

    @Override
    public Equipment viewEquipmentDetails(UUID equipmentId) {
        return equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new EquipmentException("Equipment not found with id: " + equipmentId));
    }

    @Override
    public PageResult<Equipment> getListEquipment(PageInfo pageInfo) {
        return equipmentRepository.findAll(pageInfo);
    }

    @Override
    public PageResult<Equipment> checkEquipmentInventory(PageInfo pageInfo) {
        return equipmentRepository.findAll(pageInfo);
    }

    @Override
    public Equipment checkEquipmentInventoryById(UUID equipmentId) {
        return viewEquipmentDetails(equipmentId);
    }

    @Override
    public void addEquipmentToInventory(UUID equipmentId, Integer quantity) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new EquipmentException("Equipment not found with id: " + equipmentId));

        equipment.addToInventory(quantity);
        equipment.setUpdatedAt(LocalDateTime.now());
        equipmentRepository.update(equipment);
    }

    @Override
    public void removeEquipmentFromInventory(UUID equipmentId, Integer quantity) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new EquipmentException("Equipment not found with id: " + equipmentId));

        try {
            equipment.removeFromInventory(quantity);
            equipment.setUpdatedAt(LocalDateTime.now());
            equipmentRepository.update(equipment);
        } catch (IllegalArgumentException e) {
            throw new EquipmentException(e.getMessage());
        }
    }

    @Override
    public EquipmentLoan borrowEquipment(BorrowEquipmentCommand command) {
        Equipment equipment = equipmentRepository.findById(command.getEquipmentId())
                .orElseThrow(() -> new EquipmentException("Equipment not found with id: " + command.getEquipmentId()));

        if (!equipment.isAvailableForBorrow()) {
            throw new EquipmentException("Equipment is not available for borrowing");
        }

        if (command.getQuantity() > equipment.getAvailableQuantity()) {
            throw new EquipmentException("Not enough equipment available for borrowing");
        }

        // Create loan record
        EquipmentLoan loan = EquipmentLoan.builder()
                .id(UUID.randomUUID())
                .equipmentId(command.getEquipmentId())
                .borrowerId(command.getBorrowerId())
                .borrowerType(command.getBorrowerType())
                .quantity(command.getQuantity())
                .borrowDate(LocalDateTime.now())
                .expectedReturnDate(command.getExpectedReturnDate())
                .status(EquipmentLoanStatus.BORROWED)
                .purpose(command.getPurpose())
                .notes(command.getNotes())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Update equipment inventory
        equipment.borrowEquipment(command.getQuantity());
        equipment.setUpdatedAt(LocalDateTime.now());
        equipmentRepository.update(equipment);

        return equipmentLoanRepository.create(loan);
    }

    @Override
    public EquipmentLoan returnEquipment(ReturnEquipmentCommand command) {
        EquipmentLoan loan = equipmentLoanRepository.findById(command.getLoanId())
                .orElseThrow(() -> new EquipmentException("Loan not found with id: " + command.getLoanId()));

        if (!loan.isActive()) {
            throw new EquipmentException("Loan is not active");
        }

        Equipment equipment = equipmentRepository.findById(loan.getEquipmentId())
                .orElseThrow(() -> new EquipmentException("Equipment not found"));

        // Return equipment to inventory
        equipment.returnEquipment(loan.getQuantity());
        equipment.setUpdatedAt(LocalDateTime.now());
        equipmentRepository.update(equipment);

        // Update loan record
        loan.returnEquipment(command.getReturnCondition(), command.getReturnNotes());
        
        return equipmentLoanRepository.update(loan);
    }

    @Override
    public PageResult<EquipmentLoan> trackEquipmentLoan(PageInfo pageInfo) {
        return equipmentLoanRepository.findAll(pageInfo);
    }

    @Override
    public EquipmentLoan trackEquipmentLoanById(UUID loanId) {
        return equipmentLoanRepository.findById(loanId)
                .orElseThrow(() -> new EquipmentException("Loan not found with id: " + loanId));
    }

    @Override
    public EquipmentMaintenance scheduleMaintenance(ScheduleMaintenanceCommand command) {
        Equipment equipment = equipmentRepository.findById(command.getEquipmentId())
                .orElseThrow(() -> new EquipmentException("Equipment not found with id: " + command.getEquipmentId()));

        EquipmentMaintenance maintenance = EquipmentMaintenance.builder()
                .id(UUID.randomUUID())
                .equipmentId(command.getEquipmentId())
                .type(command.getType())
                .status(MaintenanceStatus.SCHEDULED)
                .description(command.getDescription())
                .scheduledDate(command.getScheduledDate())
                .assignedTechnicianId(command.getAssignedTechnicianId())
                .externalServiceProvider(command.getExternalServiceProvider())
                .estimatedCost(command.getEstimatedCost())
                .notes(command.getNotes())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return equipmentMaintenanceRepository.create(maintenance);
    }

    @Override
    public EquipmentMaintenance recordRepairDetails(RecordRepairCommand command) {
        EquipmentMaintenance maintenance = equipmentMaintenanceRepository.findById(command.getMaintenanceId())
                .orElseThrow(() -> new EquipmentException("Maintenance record not found with id: " + command.getMaintenanceId()));

        maintenance.completeMaintenance(command.getActualCost(), command.getRepairNotes());
        maintenance.setPartsUsed(command.getPartsUsed());
        maintenance.setMaterialsUsed(command.getMaterialsUsed());

        return equipmentMaintenanceRepository.update(maintenance);
    }
}
