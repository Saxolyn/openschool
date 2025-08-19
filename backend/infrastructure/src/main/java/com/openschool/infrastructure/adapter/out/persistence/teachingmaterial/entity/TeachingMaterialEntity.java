package com.openschool.infrastructure.adapter.out.persistence.teachingmaterial.entity;

import com.openschool.domain.teachingmaterial.MaterialStatus;
import com.openschool.domain.teachingmaterial.MaterialType;
import com.openschool.domain.teachingmaterial.TeachingMaterial;
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
@Table(name = "teaching_materials")
public class TeachingMaterialEntity extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(nullable = false)
    private String title;
    
    private String description;
    
    @Enumerated(EnumType.STRING)
    private MaterialType type;
    
    private String fileName;
    private String filePath;
    private String fileUrl;
    private Long fileSize;
    private String mimeType;
    
    private UUID subjectId;
    private UUID gradeId;
    private UUID classId;
    private UUID teacherId;
    private String tags;
    
    @Enumerated(EnumType.STRING)
    private MaterialStatus status;
    
    private boolean isPublic;
    private boolean isApproved;
    private UUID approvedById;
    private LocalDateTime approvedAt;
    
    private Integer downloadCount;
    private Integer viewCount;
    
    private UUID schoolId;
    private UUID academicYearId;
    
    public static TeachingMaterialEntity fromDomain(TeachingMaterial material) {
        if (material == null) return null;
        
        return TeachingMaterialEntity.builder()
                .id(material.getId())
                .title(material.getTitle())
                .description(material.getDescription())
                .type(material.getType())
                .fileName(material.getFileName())
                .filePath(material.getFilePath())
                .fileUrl(material.getFileUrl())
                .fileSize(material.getFileSize())
                .mimeType(material.getMimeType())
                .subjectId(material.getSubjectId())
                .gradeId(material.getGradeId())
                .classId(material.getClassId())
                .teacherId(material.getTeacherId())
                .tags(material.getTags())
                .status(material.getStatus())
                .isPublic(material.isPublic())
                .isApproved(material.isApproved())
                .approvedById(material.getApprovedById())
                .approvedAt(material.getApprovedAt())
                .downloadCount(material.getDownloadCount())
                .viewCount(material.getViewCount())
                .schoolId(material.getSchoolId())
                .academicYearId(material.getAcademicYearId())
                .build();
    }
    
    public TeachingMaterial toDomain() {
        return TeachingMaterial.builder()
                .id(this.id)
                .title(this.title)
                .description(this.description)
                .type(this.type)
                .fileName(this.fileName)
                .filePath(this.filePath)
                .fileUrl(this.fileUrl)
                .fileSize(this.fileSize)
                .mimeType(this.mimeType)
                .subjectId(this.subjectId)
                .gradeId(this.gradeId)
                .classId(this.classId)
                .teacherId(this.teacherId)
                .tags(this.tags)
                .status(this.status)
                .isPublic(this.isPublic)
                .isApproved(this.isApproved)
                .approvedById(this.approvedById)
                .approvedAt(this.approvedAt)
                .downloadCount(this.downloadCount)
                .viewCount(this.viewCount)
                .schoolId(this.schoolId)
                .academicYearId(this.academicYearId)
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }
}
