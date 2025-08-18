package com.openschool.infrastructure.adapter.out.file;

import com.openschool.common.file.exception.FileProcessingException;
import com.openschool.common.file.port.FileStoragePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Local file system implementation of FileStoragePort
 */
@Component
public class LocalFileStorageAdapter implements FileStoragePort {
    
    @Value("${app.file.upload.dir:uploads}")
    private String uploadDir;
    
    @Value("${app.file.thumbnail.dir:thumbnails}")
    private String thumbnailDir;
    
    @Override
    public String storeFile(byte[] fileData, String fileName, String contentType) {
        try {
            // Create directory structure: uploads/yyyy/MM/dd/
            String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            Path targetDir = Paths.get(uploadDir, dateDir);
            Files.createDirectories(targetDir);
            
            // Create full file path
            Path targetPath = targetDir.resolve(fileName);
            
            // Write file
            Files.write(targetPath, fileData);
            
            return targetPath.toString();
            
        } catch (IOException e) {
            throw new FileProcessingException("Failed to store file: " + fileName, e);
        }
    }
    
    @Override
    public String storeFile(InputStream inputStream, String fileName, String contentType, long contentLength) {
        try {
            // Create directory structure
            String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            Path targetDir = Paths.get(uploadDir, dateDir);
            Files.createDirectories(targetDir);
            
            // Create full file path
            Path targetPath = targetDir.resolve(fileName);
            
            // Copy from input stream
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            
            return targetPath.toString();
            
        } catch (IOException e) {
            throw new FileProcessingException("Failed to store file: " + fileName, e);
        }
    }
    
    @Override
    public byte[] retrieveFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                throw new FileProcessingException("File not found: " + filePath);
            }
            
            return Files.readAllBytes(path);
            
        } catch (IOException e) {
            throw new FileProcessingException("Failed to retrieve file: " + filePath, e);
        }
    }
    
    @Override
    public InputStream getFileStream(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                throw new FileProcessingException("File not found: " + filePath);
            }
            
            return Files.newInputStream(path);
            
        } catch (IOException e) {
            throw new FileProcessingException("Failed to get file stream: " + filePath, e);
        }
    }
    
    @Override
    public InputStream getFileStream(String filePath, long start, long end) {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                throw new FileProcessingException("File not found: " + filePath);
            }
            
            // For range requests, we need to create a bounded input stream
            InputStream fullStream = Files.newInputStream(path);
            
            // Skip to start position
            fullStream.skip(start);
            
            // Return bounded stream
            long contentLength = end - start + 1;
            return new BoundedInputStream(fullStream, contentLength);
            
        } catch (IOException e) {
            throw new FileProcessingException("Failed to get file stream: " + filePath, e);
        }
    }
    
    @Override
    public boolean deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            return Files.deleteIfExists(path);
            
        } catch (IOException e) {
            throw new FileProcessingException("Failed to delete file: " + filePath, e);
        }
    }
    
    @Override
    public boolean fileExists(String filePath) {
        Path path = Paths.get(filePath);
        return Files.exists(path);
    }
    
    @Override
    public long getFileSize(String filePath) {
        try {
            Path path = Paths.get(filePath);
            return Files.size(path);
            
        } catch (IOException e) {
            throw new FileProcessingException("Failed to get file size: " + filePath, e);
        }
    }
    
    @Override
    public String generateUniqueFileName(String originalFileName) {
        String extension = "";
        if (originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        
        return UUID.randomUUID().toString() + extension;
    }
    
    @Override
    public String generateThumbnail(String filePath, int width, int height) {
        try {
            Path sourcePath = Paths.get(filePath);
            if (!Files.exists(sourcePath)) {
                throw new FileProcessingException("Source file not found: " + filePath);
            }
            
            // Read original image
            BufferedImage originalImage = ImageIO.read(sourcePath.toFile());
            if (originalImage == null) {
                throw new FileProcessingException("Cannot read image file: " + filePath);
            }
            
            // Calculate thumbnail dimensions maintaining aspect ratio
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();
            
            double aspectRatio = (double) originalWidth / originalHeight;
            int thumbnailWidth = width;
            int thumbnailHeight = height;
            
            if (aspectRatio > 1) {
                thumbnailHeight = (int) (width / aspectRatio);
            } else {
                thumbnailWidth = (int) (height * aspectRatio);
            }
            
            // Create thumbnail
            BufferedImage thumbnailImage = new BufferedImage(thumbnailWidth, thumbnailHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = thumbnailImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(originalImage, 0, 0, thumbnailWidth, thumbnailHeight, null);
            g2d.dispose();
            
            // Create thumbnail directory
            String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            Path thumbnailDirPath = Paths.get(thumbnailDir, dateDir);
            Files.createDirectories(thumbnailDirPath);
            
            // Generate thumbnail file name
            String thumbnailFileName = "thumb_" + UUID.randomUUID().toString() + ".jpg";
            Path thumbnailPath = thumbnailDirPath.resolve(thumbnailFileName);
            
            // Save thumbnail
            ImageIO.write(thumbnailImage, "jpg", thumbnailPath.toFile());
            
            return thumbnailPath.toString();
            
        } catch (IOException e) {
            throw new FileProcessingException("Failed to generate thumbnail: " + filePath, e);
        }
    }
    
    // Helper class for bounded input stream
    private static class BoundedInputStream extends InputStream {
        private final InputStream inputStream;
        private long remaining;
        
        public BoundedInputStream(InputStream inputStream, long maxBytes) {
            this.inputStream = inputStream;
            this.remaining = maxBytes;
        }
        
        @Override
        public int read() throws IOException {
            if (remaining <= 0) {
                return -1;
            }
            int result = inputStream.read();
            if (result != -1) {
                remaining--;
            }
            return result;
        }
        
        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            if (remaining <= 0) {
                return -1;
            }
            int toRead = (int) Math.min(len, remaining);
            int result = inputStream.read(b, off, toRead);
            if (result > 0) {
                remaining -= result;
            }
            return result;
        }
        
        @Override
        public void close() throws IOException {
            inputStream.close();
        }
    }
}
