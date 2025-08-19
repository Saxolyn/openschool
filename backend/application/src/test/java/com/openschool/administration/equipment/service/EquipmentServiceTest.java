package com.openschool.administration.equipment.service;

import com.openschool.administration.equipment.exception.EquipmentException;
import com.openschool.administration.equipment.port.in.command.CreateEquipmentCommand;
import com.openschool.administration.equipment.port.in.command.UpdateEquipmentCommand;
import com.openschool.administration.equipment.port.out.EquipmentLoanRepositoryPort;
import com.openschool.administration.equipment.port.out.EquipmentMaintenanceRepositoryPort;
import com.openschool.administration.equipment.port.out.EquipmentRepositoryPort;
import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.equipment.Equipment;
import com.openschool.domain.equipment.EquipmentCondition;
import com.openschool.domain.equipment.EquipmentStatus;
import com.openschool.domain.equipment.EquipmentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipmentServiceTest {

    @Mock
    private EquipmentRepositoryPort equipmentRepository;

    @Mock
    private EquipmentLoanRepositoryPort equipmentLoanRepository;

    @Mock
    private EquipmentMaintenanceRepositoryPort equipmentMaintenanceRepository;

    @InjectMocks
    private EquipmentService equipmentService;

    private CreateEquipmentCommand createCommand;
    private Equipment equipment;
    private UUID equipmentId;

    @BeforeEach
    void setUp() {
        equipmentId = UUID.randomUUID();
        
        createCommand = CreateEquipmentCommand.builder()
                .name("Test Equipment")
                .code("EQ001")
                .description("Test Description")
                .brand("Test Brand")
                .model("Test Model")
                .serialNumber("SN001")
                .schoolId(UUID.randomUUID())
                .departmentId(UUID.randomUUID())
                .location("Test Location")
                .type(EquipmentType.COMPUTER)
                .status(EquipmentStatus.ACTIVE)
                .condition(EquipmentCondition.GOOD)
                .purchasePrice(1000.0)
                .purchaseDate(LocalDateTime.now())
                .supplier("Test Supplier")
                .quantity(5)
                .build();

        equipment = Equipment.builder()
                .id(equipmentId)
                .name("Test Equipment")
                .code("EQ001")
                .description("Test Description")
                .brand("Test Brand")
                .model("Test Model")
                .serialNumber("SN001")
                .schoolId(createCommand.getSchoolId())
                .departmentId(createCommand.getDepartmentId())
                .location("Test Location")
                .type(EquipmentType.COMPUTER)
                .status(EquipmentStatus.ACTIVE)
                .condition(EquipmentCondition.GOOD)
                .purchasePrice(1000.0)
                .purchaseDate(createCommand.getPurchaseDate())
                .supplier("Test Supplier")
                .quantity(5)
                .availableQuantity(5)
                .borrowedQuantity(0)
                .maintenanceQuantity(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createEquipment_Success() {
        // Given
        when(equipmentRepository.existsByCode(anyString())).thenReturn(false);
        when(equipmentRepository.existsBySerialNumber(anyString())).thenReturn(false);
        when(equipmentRepository.create(any(Equipment.class))).thenReturn(equipment);

        // When
        Equipment result = equipmentService.createEquipment(createCommand);

        // Then
        assertNotNull(result);
        assertEquals("Test Equipment", result.getName());
        assertEquals("EQ001", result.getCode());
        verify(equipmentRepository).existsByCode("EQ001");
        verify(equipmentRepository).existsBySerialNumber("SN001");
        verify(equipmentRepository).create(any(Equipment.class));
    }

    @Test
    void createEquipment_DuplicateCode_ThrowsException() {
        // Given
        when(equipmentRepository.existsByCode(anyString())).thenReturn(true);

        // When & Then
        EquipmentException exception = assertThrows(EquipmentException.class, 
            () -> equipmentService.createEquipment(createCommand));
        
        assertEquals("Equipment with code EQ001 already exists", exception.getMessage());
        verify(equipmentRepository).existsByCode("EQ001");
        verify(equipmentRepository, never()).create(any(Equipment.class));
    }

    @Test
    void createEquipment_DuplicateSerialNumber_ThrowsException() {
        // Given
        when(equipmentRepository.existsByCode(anyString())).thenReturn(false);
        when(equipmentRepository.existsBySerialNumber(anyString())).thenReturn(true);

        // When & Then
        EquipmentException exception = assertThrows(EquipmentException.class, 
            () -> equipmentService.createEquipment(createCommand));
        
        assertEquals("Equipment with serial number SN001 already exists", exception.getMessage());
        verify(equipmentRepository).existsBySerialNumber("SN001");
        verify(equipmentRepository, never()).create(any(Equipment.class));
    }

    @Test
    void updateEquipmentDetails_Success() {
        // Given
        UpdateEquipmentCommand updateCommand = UpdateEquipmentCommand.builder()
                .id(equipmentId)
                .name("Updated Equipment")
                .code("EQ001")
                .description("Updated Description")
                .type(EquipmentType.PROJECTOR)
                .status(EquipmentStatus.ACTIVE)
                .condition(EquipmentCondition.EXCELLENT)
                .build();

        when(equipmentRepository.findById(equipmentId)).thenReturn(Optional.of(equipment));
        when(equipmentRepository.existsByCode("EQ001")).thenReturn(false);
        when(equipmentRepository.update(any(Equipment.class))).thenReturn(equipment);

        // When
        Equipment result = equipmentService.updateEquipmentDetails(updateCommand);

        // Then
        assertNotNull(result);
        verify(equipmentRepository).findById(equipmentId);
        verify(equipmentRepository).update(any(Equipment.class));
    }

    @Test
    void updateEquipmentDetails_NotFound_ThrowsException() {
        // Given
        UpdateEquipmentCommand updateCommand = UpdateEquipmentCommand.builder()
                .id(equipmentId)
                .name("Updated Equipment")
                .code("EQ001")
                .build();

        when(equipmentRepository.findById(equipmentId)).thenReturn(Optional.empty());

        // When & Then
        EquipmentException exception = assertThrows(EquipmentException.class, 
            () -> equipmentService.updateEquipmentDetails(updateCommand));
        
        assertEquals("Equipment not found with id: " + equipmentId, exception.getMessage());
        verify(equipmentRepository).findById(equipmentId);
        verify(equipmentRepository, never()).update(any(Equipment.class));
    }

    @Test
    void deleteEquipment_Success() {
        // Given
        when(equipmentRepository.findById(equipmentId)).thenReturn(Optional.of(equipment));
        when(equipmentLoanRepository.countByEquipmentId(equipmentId)).thenReturn(0L);
        when(equipmentRepository.delete(equipmentId)).thenReturn(true);

        // When
        assertDoesNotThrow(() -> equipmentService.deleteEquipment(equipmentId));

        // Then
        verify(equipmentRepository).findById(equipmentId);
        verify(equipmentLoanRepository).countByEquipmentId(equipmentId);
        verify(equipmentRepository).delete(equipmentId);
    }

    @Test
    void deleteEquipment_HasActiveLoans_ThrowsException() {
        // Given
        when(equipmentRepository.findById(equipmentId)).thenReturn(Optional.of(equipment));
        when(equipmentLoanRepository.countByEquipmentId(equipmentId)).thenReturn(2L);

        // When & Then
        EquipmentException exception = assertThrows(EquipmentException.class, 
            () -> equipmentService.deleteEquipment(equipmentId));
        
        assertEquals("Cannot delete equipment with active loans", exception.getMessage());
        verify(equipmentRepository).findById(equipmentId);
        verify(equipmentLoanRepository).countByEquipmentId(equipmentId);
        verify(equipmentRepository, never()).delete(equipmentId);
    }

    @Test
    void viewEquipmentDetails_Success() {
        // Given
        when(equipmentRepository.findById(equipmentId)).thenReturn(Optional.of(equipment));

        // When
        Equipment result = equipmentService.viewEquipmentDetails(equipmentId);

        // Then
        assertNotNull(result);
        assertEquals(equipmentId, result.getId());
        assertEquals("Test Equipment", result.getName());
        verify(equipmentRepository).findById(equipmentId);
    }

    @Test
    void viewEquipmentDetails_NotFound_ThrowsException() {
        // Given
        when(equipmentRepository.findById(equipmentId)).thenReturn(Optional.empty());

        // When & Then
        EquipmentException exception = assertThrows(EquipmentException.class, 
            () -> equipmentService.viewEquipmentDetails(equipmentId));
        
        assertEquals("Equipment not found with id: " + equipmentId, exception.getMessage());
        verify(equipmentRepository).findById(equipmentId);
    }

    @Test
    void getListEquipment_Success() {
        // Given
        PageInfo pageInfo = new PageInfo(0, 10);
        PageResult<Equipment> expectedResult = new PageResult<>(0, 10, null, 1L, 1L);
        when(equipmentRepository.findAll(pageInfo)).thenReturn(expectedResult);

        // When
        PageResult<Equipment> result = equipmentService.getListEquipment(pageInfo);

        // Then
        assertNotNull(result);
        assertEquals(expectedResult, result);
        verify(equipmentRepository).findAll(pageInfo);
    }

    @Test
    void addEquipmentToInventory_Success() {
        // Given
        Integer quantityToAdd = 3;
        when(equipmentRepository.findById(equipmentId)).thenReturn(Optional.of(equipment));
        when(equipmentRepository.update(any(Equipment.class))).thenReturn(equipment);

        // When
        assertDoesNotThrow(() -> equipmentService.addEquipmentToInventory(equipmentId, quantityToAdd));

        // Then
        verify(equipmentRepository).findById(equipmentId);
        verify(equipmentRepository).update(any(Equipment.class));
    }

    @Test
    void removeEquipmentFromInventory_Success() {
        // Given
        Integer quantityToRemove = 2;
        when(equipmentRepository.findById(equipmentId)).thenReturn(Optional.of(equipment));
        when(equipmentRepository.update(any(Equipment.class))).thenReturn(equipment);

        // When
        assertDoesNotThrow(() -> equipmentService.removeEquipmentFromInventory(equipmentId, quantityToRemove));

        // Then
        verify(equipmentRepository).findById(equipmentId);
        verify(equipmentRepository).update(any(Equipment.class));
    }

    @Test
    void removeEquipmentFromInventory_InsufficientQuantity_ThrowsException() {
        // Given
        Integer quantityToRemove = 10; // More than available
        when(equipmentRepository.findById(equipmentId)).thenReturn(Optional.of(equipment));

        // When & Then
        EquipmentException exception = assertThrows(EquipmentException.class, 
            () -> equipmentService.removeEquipmentFromInventory(equipmentId, quantityToRemove));
        
        assertEquals("Cannot remove more equipment than available", exception.getMessage());
        verify(equipmentRepository).findById(equipmentId);
        verify(equipmentRepository, never()).update(any(Equipment.class));
    }
}
