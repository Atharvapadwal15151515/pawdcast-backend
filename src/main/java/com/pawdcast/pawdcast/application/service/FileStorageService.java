package com.pawdcast.pawdcast.application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    public String storeFile(MultipartFile file) throws IOException {
        try {
            // Debug: Show upload directory info
            System.out.println("=== FILE STORAGE DEBUG ===");
            System.out.println("Upload directory setting: " + uploadDir);

            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            System.out.println("Absolute upload path: " + uploadPath.toAbsolutePath());

            if (!Files.exists(uploadPath)) {
                System.out.println("Upload directory doesn't exist, creating...");
                Files.createDirectories(uploadPath);
                System.out.println("Upload directory created successfully");
            } else {
                System.out.println("Upload directory already exists");
            }

            // Check directory permissions
            System.out.println("Directory is readable: " + Files.isReadable(uploadPath));
            System.out.println("Directory is writable: " + Files.isWritable(uploadPath));

            // Generate unique filename
            String originalFileName = file.getOriginalFilename();
            System.out.println("Original filename: " + originalFileName);
            System.out.println("File size: " + file.getSize() + " bytes");
            System.out.println("File content type: " + file.getContentType());

            String fileExtension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                System.out.println("File extension: " + fileExtension);
            } else {
                System.out.println("No file extension found");
            }

            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
            System.out.println("Generated unique filename: " + uniqueFileName);

            // Copy file to target location
            Path targetLocation = uploadPath.resolve(uniqueFileName);
            System.out.println("Target file location: " + targetLocation.toAbsolutePath());

            // Check if we can write to the target location
            try {
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File copied successfully to: " + targetLocation.toAbsolutePath());

                // Verify the file was created
                boolean fileExists = Files.exists(targetLocation);
                long fileSize = Files.size(targetLocation);
                System.out.println("File exists after copy: " + fileExists);
                System.out.println("File size after copy: " + fileSize + " bytes");

                if (fileExists && fileSize > 0) {
                    System.out.println("=== FILE STORAGE SUCCESS ===");
                    return uniqueFileName;
                } else {
                    throw new IOException("File was not created properly or is empty");
                }

            } catch (IOException e) {
                System.out.println("Error during file copy: " + e.getMessage());
                throw e;
            }

        } catch (Exception e) {
            System.out.println("=== FILE STORAGE ERROR ===");
            System.out.println("Error in storeFile: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Path loadFile(String filename) {
        try {
            System.out.println("Loading file: " + filename);
            Path filePath = Paths.get(uploadDir).resolve(filename);
            System.out.println("Full file path: " + filePath.toAbsolutePath());

            if (!Files.exists(filePath)) {
                System.out.println("File not found: " + filePath.toAbsolutePath());
                throw new IOException("File not found: " + filename);
            }

            System.out.println("File found, size: " + Files.size(filePath) + " bytes");
            return filePath;

        } catch (Exception e) {
            System.out.println("Error loading file '" + filename + "': " + e.getMessage());
            throw new RuntimeException("Error loading file: " + e.getMessage(), e);
        }
    }

    public boolean deleteFile(String filename) {
        try {
            System.out.println("Attempting to delete file: " + filename);
            Path filePath = loadFile(filename);

            boolean deleted = Files.deleteIfExists(filePath);
            if (deleted) {
                System.out.println("File deleted successfully: " + filename);
            } else {
                System.out.println("File did not exist or could not be deleted: " + filename);
            }

            return deleted;

        } catch (IOException e) {
            System.out.println("Error deleting file '" + filename + "': " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Unexpected error deleting file '" + filename + "': " + e.getMessage());
            return false;
        }
    }

    // Helper method to check upload directory status
    public void checkUploadDirectory() {
        try {
            Path uploadPath = Paths.get(uploadDir);
            System.out.println("=== UPLOAD DIRECTORY STATUS ===");
            System.out.println("Configured directory: " + uploadDir);
            System.out.println("Absolute path: " + uploadPath.toAbsolutePath());
            System.out.println("Exists: " + Files.exists(uploadPath));

            if (Files.exists(uploadPath)) {
                System.out.println("Is directory: " + Files.isDirectory(uploadPath));
                System.out.println("Is readable: " + Files.isReadable(uploadPath));
                System.out.println("Is writable: " + Files.isWritable(uploadPath));
                System.out.println("Is executable: " + Files.isExecutable(uploadPath));
            }
            System.out.println("=== END DIRECTORY STATUS ===");
        } catch (Exception e) {
            System.out.println("Error checking upload directory: " + e.getMessage());
        }
    }
}