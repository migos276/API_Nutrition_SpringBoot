package com.allergydetection.controller;

import com.allergydetection.dto.FoodImageDto;
import com.allergydetection.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Media Management", description = "APIs for managing media files")
public class MediaController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/foods/{foodId}/images")
    @Operation(summary = "Add image to food")
    public ResponseEntity<Map<String, Object>> addFoodImage(@PathVariable Long foodId, @RequestBody AddImageRequest request) {
        try {
            FoodImageDto imageDto = imageService.downloadAndSaveImage(
                    request.getImageUrl(), 
                    foodId, 
                    "food_" + foodId, 
                    request.getIsPrimary() != null ? request.getIsPrimary() : false
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Image ajoutée avec succès");
            response.put("image_data", imageDto);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/foods/{foodId}/images")
    @Operation(summary = "Get all images for a food")
    public ResponseEntity<Map<String, Object>> getFoodImages(@PathVariable Long foodId) {
        List<FoodImageDto> images = imageService.getFoodImages(foodId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("food_id", foodId);
        response.put("images", images);
        response.put("total_images", images.size());
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/images/{imageId}/primary")
    @Operation(summary = "Set image as primary")
    public ResponseEntity<Map<String, Object>> setPrimaryImage(@PathVariable Long imageId) {
        imageService.setPrimaryImage(imageId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Image définie comme principale");
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/images/{imageId}")
    @Operation(summary = "Delete image")
    public ResponseEntity<Map<String, Object>> deleteImage(@PathVariable Long imageId) {
        imageService.deleteImage(imageId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Image supprimée avec succès");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/media/{fileName}")
    @Operation(summary = "Serve media files")
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName) {
        Resource resource = imageService.loadFileAsResource(fileName);
        
        String contentType = "application/octet-stream";
        if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")) {
            contentType = "image/jpeg";
        } else if (fileName.toLowerCase().endsWith(".png")) {
            contentType = "image/png";
        } else if (fileName.toLowerCase().endsWith(".gif")) {
            contentType = "image/gif";
        } else if (fileName.toLowerCase().endsWith(".webp")) {
            contentType = "image/webp";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    // Inner class for request DTO
    public static class AddImageRequest {
        private String imageUrl;
        private Boolean isPrimary;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public Boolean getIsPrimary() {
            return isPrimary;
        }

        public void setIsPrimary(Boolean isPrimary) {
            this.isPrimary = isPrimary;
        }
    }
}