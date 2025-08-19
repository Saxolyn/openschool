package com.openschool.education.teachingMaterial.service;

import com.openschool.education.teachingMaterial.port.in.command.CreateTeachingMaterialCommand;
import com.openschool.education.teachingMaterial.port.out.TeachingMaterialRepositoryPort;
import com.openschool.domain.teachingmaterial.MaterialStatus;
import com.openschool.domain.teachingmaterial.MaterialType;
import com.openschool.domain.teachingmaterial.TeachingMaterial;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeachingMaterialServiceTest {

    @Mock
    private TeachingMaterialRepositoryPort teachingMaterialRepository;

    @InjectMocks
    private TeachingMaterialService teachingMaterialService;

    private CreateTeachingMaterialCommand createCommand;
    private TeachingMaterial teachingMaterial;

    @BeforeEach
    void setUp() {
        createCommand = CreateTeachingMaterialCommand.builder()
                .title("Mathematics Worksheet")
                .description("Algebra practice problems")
                .type(MaterialType.WORKSHEET)
                .fileName("algebra_worksheet.pdf")
                .filePath("/materials/algebra_worksheet.pdf")
                .fileUrl("https://example.com/materials/algebra_worksheet.pdf")
                .fileSize(1024L)
                .mimeType("application/pdf")
                .subjectId(UUID.randomUUID())
                .gradeId(UUID.randomUUID())
                .classId(UUID.randomUUID())
                .teacherId(UUID.randomUUID())
                .tags("algebra,math,worksheet")
                .isPublic(true)
                .schoolId(UUID.randomUUID())
                .academicYearId(UUID.randomUUID())
                .build();

        teachingMaterial = TeachingMaterial.builder()
                .id(UUID.randomUUID())
                .title(createCommand.getTitle())
                .description(createCommand.getDescription())
                .type(createCommand.getType())
                .status(MaterialStatus.ACTIVE)
                .isPublic(createCommand.isPublic())
                .isApproved(false)
                .downloadCount(0)
                .viewCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createTeachingMaterial_Success() {
        // Given
        when(teachingMaterialRepository.create(any(TeachingMaterial.class))).thenReturn(teachingMaterial);

        // When
        TeachingMaterial result = teachingMaterialService.createTeachingMaterial(createCommand);

        // Then
        assertNotNull(result);
        assertEquals("Mathematics Worksheet", result.getTitle());
        assertEquals(MaterialType.WORKSHEET, result.getType());
        assertEquals(MaterialStatus.ACTIVE, result.getStatus());
        assertTrue(result.isPublic());
        assertFalse(result.isApproved());
        assertEquals(0, result.getDownloadCount());
        assertEquals(0, result.getViewCount());
        verify(teachingMaterialRepository).create(any(TeachingMaterial.class));
    }

    @Test
    void viewTeachingMaterial_Success() {
        // Given
        when(teachingMaterialRepository.findById(teachingMaterial.getId())).thenReturn(Optional.of(teachingMaterial));
        when(teachingMaterialRepository.update(any(TeachingMaterial.class))).thenReturn(teachingMaterial);

        // When
        TeachingMaterial result = teachingMaterialService.viewTeachingMaterial(teachingMaterial.getId());

        // Then
        assertNotNull(result);
        assertEquals(teachingMaterial.getId(), result.getId());
        verify(teachingMaterialRepository).findById(teachingMaterial.getId());
        verify(teachingMaterialRepository).update(any(TeachingMaterial.class)); // For incrementing view count
    }

    @Test
    void viewTeachingMaterial_NotFound_ThrowsException() {
        // Given
        when(teachingMaterialRepository.findById(teachingMaterial.getId())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> teachingMaterialService.viewTeachingMaterial(teachingMaterial.getId()));
    }

    @Test
    void downloadTeachingMaterial_Success() {
        // Given
        teachingMaterial.setStatus(MaterialStatus.ACTIVE);
        when(teachingMaterialRepository.findById(teachingMaterial.getId())).thenReturn(Optional.of(teachingMaterial));
        when(teachingMaterialRepository.update(any(TeachingMaterial.class))).thenReturn(teachingMaterial);

        // When
        byte[] result = teachingMaterialService.downloadTeachingMaterial(teachingMaterial.getId());

        // Then
        assertNotNull(result);
        verify(teachingMaterialRepository).findById(teachingMaterial.getId());
        verify(teachingMaterialRepository).update(any(TeachingMaterial.class)); // For incrementing download count
    }
}
