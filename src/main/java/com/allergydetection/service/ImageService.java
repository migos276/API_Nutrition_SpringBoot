package com.allergydetection.service;

import com.allergydetection.config.AppConfig;
import com.allergydetection.dto.FoodImageDto;
import com.allergydetection.entity.Food;
import com.allergydetection.entity.FoodImage;
import com.allergydetection.exception.ResourceNotFoundException;
import com.allergydetection.repository.FoodImageRepository;
import com.allergydetection.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.util.stream.Collectors;

@Service
@Transactional
@EnableConfigurationProperties(AppConfig.MediaProperties.class)
public class ImageService {

    @Autowired
    private FoodImageRepository foodImageRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private WebClient webClient;

    @Autowired
    private AppConfig.MediaProperties mediaProperties;

    private final Path mediaStorageLocation;

    public ImageService(AppConfig.MediaProperties mediaProperties) {
        this.mediaProperties = mediaProperties;
        this.mediaStorageLocation = Paths.get(mediaProperties.getStoragePath()).toAbsolutePath().normalize();
        
        try {
            Files.createDirectories(this.mediaStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public FoodImageDto downloadAndSaveImage(String imageUrl, Long foodId, String foodName, Boolean isPrimary) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found with id: " + foodId));

        try {
            // Download image
            byte[] imageBytes = webClient.get()
                    .uri(imageUrl)
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();

            if (imageBytes == null || imageBytes.length == 0) {
                throw new RuntimeException("Failed to download image or image is empty");
            }

            // Determine file extension
            String fileExtension = getFileExtension(imageUrl);
            
            // Create filename
            String safeFileName = foodName.toLowerCase().replaceAll("[^a-zA-Z0-9]", "_");
            String timestamp = String.valueOf(Instant.now().getEpochSecond());
            String fileName = safeFileName + "_" + timestamp + fileExtension;

            // Save file
            Path targetLocation = this.mediaStorageLocation.resolve(fileName);
            Files.copy(new ByteArrayInputStream(imageBytes), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Reset primary images if this is primary
            if (isPrimary) {
                foodImageRepository.resetPrimaryImages(foodId);
            }

            // Save to database
            FoodImage foodImage = new FoodImage(food, targetLocation.toString(), imageUrl, isPrimary, (long) imageBytes.length);
            FoodImage savedImage = foodImageRepository.save(foodImage);

            return convertToDto(savedImage);

        } catch (Exception e) {
            throw new RuntimeException("Failed to download and save image: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<FoodImageDto> getFoodImages(Long foodId) {
        if (!foodRepository.existsById(foodId)) {
            throw new ResourceNotFoundException("Food not found with id: " + foodId);
        }

        return foodImageRepository.findByFoodIdOrderByIsPrimaryDescCreatedAtDesc(foodId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void setPrimaryImage(Long imageId) {
        FoodImage image = foodImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + imageId));

        // Reset all primary images for this food
        foodImageRepository.resetPrimaryImages(image.getFood().getId());

        // Set this image as primary
        image.setIsPrimary(true);
        foodImageRepository.save(image);
    }

    public void deleteImage(Long imageId) {
        FoodImage image = foodImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + imageId));

        try {
            // Delete file from filesystem
            Path filePath = Paths.get(image.getFilePath());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // Log error but continue with database deletion
            System.err.println("Failed to delete image file: " + e.getMessage());
        }

        // Delete from database
        foodImageRepository.delete(image);
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.mediaStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("File not found: " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new ResourceNotFoundException("File not found: " + fileName);
        }
    }

    private String getFileExtension(String url) {
        String[] parts = url.split("\\.");
        if (parts.length > 1) {
            String extension = parts[parts.length - 1].toLowerCase();
            if (extension.contains("?")) {
                extension = extension.split("\\?")[0];
            }
            
            // Validate extension
            if (extension.matches("jpg|jpeg|png|gif|webp")) {
                return "." + extension;
            }
        }
        return ".jpg"; // Default extension
    }

    private FoodImageDto convertToDto(FoodImage image) {
        String imageUrl = "/api/media/" + extractFilename(image.getFilePath());
        return new FoodImageDto(
                image.getId(),
                image.getFilePath(),
                imageUrl,
                image.getOriginalUrl(),
                image.getIsPrimary(),
                image.getFileSize(),
                image.getCreatedAt()
        );
    }

    private String extractFilename(String filePath) {
        if (filePath == null) return null;
        return filePath.substring(filePath.lastIndexOf('/') + 1);
    }
}