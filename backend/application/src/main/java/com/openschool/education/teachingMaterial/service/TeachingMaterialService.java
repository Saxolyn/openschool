package com.openschool.education.teachingMaterial.service;

import com.openschool.education.teachingMaterial.port.in.*;
import com.openschool.education.teachingMaterial.port.in.command.*;
import com.openschool.education.teachingMaterial.port.out.*;
import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.teachingmaterial.*;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class TeachingMaterialService implements
        CreateTeachingMaterialUseCase,
        ViewTeachingMaterialUseCase,
        DownloadTeachingMaterialUseCase {

    private final TeachingMaterialRepositoryPort teachingMaterialRepository;

    @Override
    public TeachingMaterial createTeachingMaterial(CreateTeachingMaterialCommand command) {
        TeachingMaterial material = TeachingMaterial.builder()
                .id(UUID.randomUUID())
                .title(command.getTitle())
                .description(command.getDescription())
                .type(command.getType())
                .fileName(command.getFileName())
                .filePath(command.getFilePath())
                .fileUrl(command.getFileUrl())
                .fileSize(command.getFileSize())
                .mimeType(command.getMimeType())
                .subjectId(command.getSubjectId())
                .gradeId(command.getGradeId())
                .classId(command.getClassId())
                .teacherId(command.getTeacherId())
                .tags(command.getTags())
                .status(MaterialStatus.ACTIVE)
                .isPublic(command.isPublic())
                .isApproved(false)
                .downloadCount(0)
                .viewCount(0)
                .schoolId(command.getSchoolId())
                .academicYearId(command.getAcademicYearId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return teachingMaterialRepository.create(material);
    }

    @Override
    public TeachingMaterial viewTeachingMaterial(UUID materialId) {
        TeachingMaterial material = teachingMaterialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Teaching material not found"));

        // Increment view count
        material.incrementViewCount();
        teachingMaterialRepository.update(material);

        return material;
    }

    @Override
    public PageResult<TeachingMaterial> viewTeachingMaterialsBySubject(UUID subjectId, PageInfo pageInfo) {
        return teachingMaterialRepository.findBySubjectId(subjectId, pageInfo);
    }

    @Override
    public byte[] downloadTeachingMaterial(UUID materialId) {
        TeachingMaterial material = teachingMaterialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Teaching material not found"));

        if (!material.isDownloadable()) {
            throw new RuntimeException("Material is not available for download");
        }

        // Increment download count
        material.incrementDownloadCount();
        teachingMaterialRepository.update(material);

        // In a real implementation, this would read the file from storage
        // For now, return empty byte array
        return new byte[0];
    }
}
