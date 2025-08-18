package com.openschool.infrastructure.config;

import com.openschool.common.file.port.FileMetadataRepositoryPort;
import com.openschool.common.file.port.FileStoragePort;
import com.openschool.common.file.service.FileService;
import com.openschool.common.file.usecase.DownloadFileUseCase;
import com.openschool.common.file.usecase.StreamFileUseCase;
import com.openschool.common.file.usecase.UploadFileUseCase;
import com.openschool.infrastructure.adapter.out.file.LocalFileStorageAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for file processing components
 */
@Configuration
public class FileConfig {
    
    @Bean
    public FileStoragePort fileStoragePort() {
        return new LocalFileStorageAdapter();
    }
    
    @Bean
    public FileService fileService(FileStoragePort fileStoragePort,
                                  FileMetadataRepositoryPort fileMetadataRepositoryPort) {
        return new FileService(fileStoragePort, fileMetadataRepositoryPort);
    }
    
    @Bean
    public UploadFileUseCase uploadFileUseCase(FileService fileService) {
        return fileService;
    }
    
    @Bean
    public DownloadFileUseCase downloadFileUseCase(FileService fileService) {
        return fileService;
    }
    
    @Bean
    public StreamFileUseCase streamFileUseCase(FileService fileService) {
        return fileService;
    }
}
