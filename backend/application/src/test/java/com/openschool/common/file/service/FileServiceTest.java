package com.openschool.common.file.service;

import com.openschool.common.file.command.DownloadFileCommand;
import com.openschool.common.file.command.StreamFileCommand;
import com.openschool.common.file.command.UploadFileCommand;
import com.openschool.common.file.exception.FileAccessDeniedException;
import com.openschool.common.file.exception.FileNotFoundException;
import com.openschool.common.file.port.FileMetadataRepositoryPort;
import com.openschool.common.file.port.FileStoragePort;
import com.openschool.common.file.result.DownloadResult;
import com.openschool.common.file.result.StreamResult;
import com.openschool.common.file.result.UploadResult;
import com.openschool.domain.common.file.FileCategory;
import com.openschool.domain.common.file.FileMetadata;
import com.openschool.domain.common.file.FileStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class FileServiceTest {

    private FileStoragePort fileStoragePort;
    private FileMetadataRepositoryPort fileMetadataRepository;
    private FileService fileService;

    @BeforeEach
    void setUp() {
        fileStoragePort = mock(FileStoragePort.class);
        fileMetadataRepository = mock(FileMetadataRepositoryPort.class);
        fileService = new FileService(fileStoragePort, fileMetadataRepository);
    }

    @Test
    void uploadFile_ShouldReturnSuccessResult_WhenValidCommand() {
        // Given
        byte[] fileData = "test file content".getBytes();
        UUID uploaderId = UUID.randomUUID();
        UploadFileCommand command = UploadFileCommand.builder()
                .fileData(fileData)
                .originalFileName("test.pdf")
                .contentType("application/pdf")
                .category(FileCategory.DOCUMENT)
                .uploadedBy(uploaderId)
                .validateFileType(true)
                .build();

        String storedFileName = "unique_test.pdf";
        String filePath = "/uploads/2024/01/01/unique_test.pdf";
        FileMetadata savedMetadata = FileMetadata.builder()
                .id(UUID.randomUUID())
                .originalFileName("test.pdf")
                .storedFileName(storedFileName)
                .contentType("application/pdf")
                .fileSize(fileData.length)
                .filePath(filePath)
                .category(FileCategory.DOCUMENT)
                .status(FileStatus.UPLOADED)
                .uploadedBy(uploaderId)
                .uploadedAt(LocalDateTime.now())
                .build();

        when(fileStoragePort.generateUniqueFileName("test.pdf")).thenReturn(storedFileName);
        when(fileStoragePort.storeFile(fileData, storedFileName, "application/pdf")).thenReturn(filePath);
        when(fileMetadataRepository.save(any(FileMetadata.class))).thenReturn(savedMetadata);

        // When
        UploadResult result = fileService.uploadFile(command);

        // Then
        assertTrue(result.isSuccessful());
        assertNotNull(result.getFileMetadata());
        assertEquals("test.pdf", result.getFileMetadata().getOriginalFileName());
        verify(fileStoragePort).storeFile(fileData, storedFileName, "application/pdf");
        verify(fileMetadataRepository).save(any(FileMetadata.class));
    }

    @Test
    void uploadFile_ShouldReturnFailureResult_WhenStorageFails() {
        // Given
        byte[] fileData = "test file content".getBytes();
        UploadFileCommand command = UploadFileCommand.builder()
                .fileData(fileData)
                .originalFileName("test.pdf")
                .contentType("application/pdf")
                .category(FileCategory.DOCUMENT)
                .uploadedBy(UUID.randomUUID())
                .validateFileType(true)
                .build();

        when(fileStoragePort.generateUniqueFileName("test.pdf")).thenReturn("unique_test.pdf");
        when(fileStoragePort.storeFile(any(), anyString(), anyString()))
                .thenThrow(new RuntimeException("Storage failed"));

        // When
        UploadResult result = fileService.uploadFile(command);

        // Then
        assertFalse(result.isSuccessful());
        assertTrue(result.getErrorMessage().contains("Upload failed"));
    }

    @Test
    void downloadFile_ShouldReturnSuccessResult_WhenFileExists() {
        // Given
        UUID fileId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        DownloadFileCommand command = new DownloadFileCommand(fileId, userId);

        byte[] fileContent = "test file content".getBytes();
        FileMetadata metadata = FileMetadata.builder()
                .id(fileId)
                .originalFileName("test.pdf")
                .contentType("application/pdf")
                .fileSize(fileContent.length)
                .filePath("/uploads/test.pdf")
                .uploadedBy(userId)
                .isPublic(false)
                .build();

        when(fileMetadataRepository.findById(fileId)).thenReturn(Optional.of(metadata));
        when(fileStoragePort.fileExists("/uploads/test.pdf")).thenReturn(true);
        when(fileStoragePort.retrieveFile("/uploads/test.pdf")).thenReturn(fileContent);

        // When
        DownloadResult result = fileService.downloadFile(command);

        // Then
        assertTrue(result.isSuccessful());
        assertNotNull(result.getFileData());
        assertEquals(metadata, result.getFileData().getMetadata());
        assertArrayEquals(fileContent, result.getFileData().getContent());
        verify(fileMetadataRepository).updateLastAccessedTime(eq(fileId), any(LocalDateTime.class));
    }

    @Test
    void downloadFile_ShouldReturnFailureResult_WhenFileNotFound() {
        // Given
        UUID fileId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        DownloadFileCommand command = new DownloadFileCommand(fileId, userId);

        when(fileMetadataRepository.findById(fileId)).thenReturn(Optional.empty());

        // When
        DownloadResult result = fileService.downloadFile(command);

        // Then
        assertFalse(result.isSuccessful());
        assertTrue(result.getErrorMessage().contains("File not found"));
    }

    @Test
    void downloadFile_ShouldReturnFailureResult_WhenAccessDenied() {
        // Given
        UUID fileId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID(); // Different from userId
        DownloadFileCommand command = new DownloadFileCommand(fileId, userId);

        FileMetadata metadata = FileMetadata.builder()
                .id(fileId)
                .uploadedBy(ownerId)
                .isPublic(false)
                .build();

        when(fileMetadataRepository.findById(fileId)).thenReturn(Optional.of(metadata));

        // When
        DownloadResult result = fileService.downloadFile(command);

        // Then
        assertFalse(result.isSuccessful());
        assertTrue(result.getErrorMessage().contains("Access denied"));
    }

    @Test
    void streamFile_ShouldReturnSuccessResult_WhenFileExists() {
        // Given
        UUID fileId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        StreamFileCommand command = new StreamFileCommand(fileId, userId);

        FileMetadata metadata = FileMetadata.builder()
                .id(fileId)
                .originalFileName("test.mp4")
                .contentType("video/mp4")
                .fileSize(1024L)
                .filePath("/uploads/test.mp4")
                .uploadedBy(userId)
                .isPublic(false)
                .build();

        InputStream inputStream = new ByteArrayInputStream("video content".getBytes());

        when(fileMetadataRepository.findById(fileId)).thenReturn(Optional.of(metadata));
        when(fileStoragePort.getFileStream("/uploads/test.mp4")).thenReturn(inputStream);

        // When
        StreamResult result = fileService.streamFile(command);

        // Then
        assertTrue(result.isSuccessful());
        assertNotNull(result.getInputStream());
        assertEquals(metadata, result.getFileMetadata());
        assertEquals(1024L, result.getContentLength());
        assertFalse(result.isPartialContent());
    }

    @Test
    void streamFile_ShouldReturnPartialContent_WhenRangeRequested() {
        // Given
        UUID fileId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        StreamFileCommand command = StreamFileCommand.builder()
                .fileId(fileId)
                .requestedBy(userId)
                .rangeStart(100)
                .rangeEnd(199)
                .build();

        FileMetadata metadata = FileMetadata.builder()
                .id(fileId)
                .originalFileName("test.mp4")
                .contentType("video/mp4")
                .fileSize(1024L)
                .filePath("/uploads/test.mp4")
                .uploadedBy(userId)
                .isPublic(false)
                .build();

        InputStream inputStream = new ByteArrayInputStream("partial content".getBytes());

        when(fileMetadataRepository.findById(fileId)).thenReturn(Optional.of(metadata));
        when(fileStoragePort.getFileStream("/uploads/test.mp4", 100, 199)).thenReturn(inputStream);

        // When
        StreamResult result = fileService.streamFile(command);

        // Then
        assertTrue(result.isSuccessful());
        assertNotNull(result.getInputStream());
        assertEquals(metadata, result.getFileMetadata());
        assertEquals(100L, result.getContentLength()); // rangeEnd - rangeStart + 1
        assertTrue(result.isPartialContent());
        assertEquals(100, result.getRangeStart());
        assertEquals(199, result.getRangeEnd());
    }

    @Test
    void canDownload_ShouldReturnTrue_WhenUserIsOwner() {
        // Given
        UUID fileId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        FileMetadata metadata = FileMetadata.builder()
                .id(fileId)
                .uploadedBy(userId)
                .isPublic(false)
                .build();

        when(fileMetadataRepository.findById(fileId)).thenReturn(Optional.of(metadata));

        // When
        boolean canDownload = fileService.canDownload(fileId, userId);

        // Then
        assertTrue(canDownload);
    }

    @Test
    void canDownload_ShouldReturnTrue_WhenFileIsPublic() {
        // Given
        UUID fileId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        FileMetadata metadata = FileMetadata.builder()
                .id(fileId)
                .uploadedBy(ownerId)
                .isPublic(true)
                .build();

        when(fileMetadataRepository.findById(fileId)).thenReturn(Optional.of(metadata));

        // When
        boolean canDownload = fileService.canDownload(fileId, userId);

        // Then
        assertTrue(canDownload);
    }

    @Test
    void canDownload_ShouldReturnFalse_WhenAccessDenied() {
        // Given
        UUID fileId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        FileMetadata metadata = FileMetadata.builder()
                .id(fileId)
                .uploadedBy(ownerId)
                .isPublic(false)
                .build();

        when(fileMetadataRepository.findById(fileId)).thenReturn(Optional.of(metadata));

        // When
        boolean canDownload = fileService.canDownload(fileId, userId);

        // Then
        assertFalse(canDownload);
    }

    @Test
    void canDownload_ShouldReturnFalse_WhenFileNotFound() {
        // Given
        UUID fileId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(fileMetadataRepository.findById(fileId)).thenReturn(Optional.empty());

        // When
        boolean canDownload = fileService.canDownload(fileId, userId);

        // Then
        assertFalse(canDownload);
    }

    @Test
    void getFileMetadata_ShouldReturnMetadata_WhenFileExists() {
        // Given
        UUID fileId = UUID.randomUUID();
        FileMetadata metadata = FileMetadata.builder()
                .id(fileId)
                .originalFileName("test.pdf")
                .build();

        when(fileMetadataRepository.findById(fileId)).thenReturn(Optional.of(metadata));

        // When
        FileMetadata result = fileService.getFileMetadata(fileId);

        // Then
        assertEquals(metadata, result);
    }

    @Test
    void getFileMetadata_ShouldThrowException_WhenFileNotFound() {
        // Given
        UUID fileId = UUID.randomUUID();

        when(fileMetadataRepository.findById(fileId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(FileNotFoundException.class, () -> fileService.getFileMetadata(fileId));
    }

    @Test
    void getMaxFileSize_ShouldReturnCorrectSize_ForValidCategory() {
        // When
        long maxSize = fileService.getMaxFileSize("IMAGE");

        // Then
        assertEquals(5L * 1024 * 1024, maxSize); // 5MB for images
    }

    @Test
    void getMaxFileSize_ShouldReturnDefaultSize_ForInvalidCategory() {
        // When
        long maxSize = fileService.getMaxFileSize("INVALID_CATEGORY");

        // Then
        assertEquals(10L * 1024 * 1024, maxSize); // Default 10MB
    }

    @Test
    void getAllowedFileTypes_ShouldReturnTypes_ForValidCategory() {
        // When
        String[] allowedTypes = fileService.getAllowedFileTypes("IMAGE");

        // Then
        assertNotNull(allowedTypes);
        assertTrue(allowedTypes.length > 0);
    }

    @Test
    void getAllowedFileTypes_ShouldReturnEmptyArray_ForInvalidCategory() {
        // When
        String[] allowedTypes = fileService.getAllowedFileTypes("INVALID_CATEGORY");

        // Then
        assertNotNull(allowedTypes);
        assertEquals(0, allowedTypes.length);
    }
}
