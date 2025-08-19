package com.openschool.education.teachingMaterial.port.in.command;

import com.openschool.domain.teachingmaterial.MaterialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTeachingMaterialCommand {
    private String title;
    private String description;
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
    private boolean isPublic;
    private UUID schoolId;
    private UUID academicYearId;
}
