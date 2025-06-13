package com.allergydetection.service;

import com.allergydetection.dto.FoodDto;
import com.allergydetection.dto.FoodImageDto;
import com.allergydetection.entity.Food;
import com.allergydetection.entity.FoodImage;
import com.allergydetection.exception.ResourceNotFoundException;
import com.allergydetection.repository.FoodImageRepository;
import com.allergydetection.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FoodImageRepository foodImageRepository;

    @Autowired
    private ImageService imageService;

    public FoodDto createFood(FoodDto foodDto) {
        Food food = new Food(foodDto.getName(), foodDto.getCategory(), foodDto.getIngredients());
        food.setImagePath(foodDto.getImagePath());
        food.setIsBaseFood(foodDto.getIsBaseFood() != null ? foodDto.getIsBaseFood() : false);

        Food savedFood = foodRepository.save(food);
        return convertToDto(savedFood);
    }

    public FoodDto createFoodWithImageUrl(FoodDto foodDto, String imageUrl) {
        Food food = new Food(foodDto.getName(), foodDto.getCategory(), foodDto.getIngredients());
        food.setIsBaseFood(foodDto.getIsBaseFood() != null ? foodDto.getIsBaseFood() : false);

        Food savedFood = foodRepository.save(food);

        // Download and save image if URL is provided
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            try {
                imageService.downloadAndSaveImage(imageUrl, savedFood.getId(), savedFood.getName(), true);
            } catch (Exception e) {
                // Log error but continue - food creation should not fail because of image download
                System.err.println("Failed to download image for food " + savedFood.getName() + ": " + e.getMessage());
            }
        }

        return convertToDto(savedFood);
    }

    @Transactional(readOnly = true)
    public FoodDto getFoodById(Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found with id: " + id));
        return convertToDtoWithImages(food);
    }

    @Transactional(readOnly = true)
    public List<FoodDto> getAllFoods() {
        return foodRepository.findAll().stream()
                .map(this::convertToDtoWithPrimaryImage)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FoodDto> searchFoods(String query) {
        return foodRepository.searchFoods(query).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FoodDto> getFoodsByCategory(String category) {
        return foodRepository.findByCategory(category).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public FoodDto updateFood(Long id, FoodDto foodDto) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found with id: " + id));

        if (foodDto.getName() != null) {
            food.setName(foodDto.getName());
        }
        if (foodDto.getCategory() != null) {
            food.setCategory(foodDto.getCategory());
        }
        if (foodDto.getIngredients() != null) {
            food.setIngredients(foodDto.getIngredients());
        }
        if (foodDto.getImagePath() != null) {
            food.setImagePath(foodDto.getImagePath());
        }
        if (foodDto.getIsBaseFood() != null) {
            food.setIsBaseFood(foodDto.getIsBaseFood());
        }

        Food updatedFood = foodRepository.save(food);
        return convertToDto(updatedFood);
    }

    public void deleteFood(Long id) {
        if (!foodRepository.existsById(id)) {
            throw new ResourceNotFoundException("Food not found with id: " + id);
        }
        foodRepository.deleteById(id);
    }

    private FoodDto convertToDto(Food food) {
        return new FoodDto(
                food.getId(),
                food.getName(),
                food.getCategory(),
                food.getIngredients(),
                food.getImagePath(),
                food.getIsBaseFood()
        );
    }

    private FoodDto convertToDtoWithPrimaryImage(Food food) {
        FoodDto dto = convertToDto(food);
        
        // Get primary image
        FoodImage primaryImage = foodImageRepository.findByFoodIdAndIsPrimary(food.getId(), true).orElse(null);
        if (primaryImage != null) {
            dto.setPrimaryImageUrl("/api/media/" + extractFilename(primaryImage.getFilePath()));
        }
        
        return dto;
    }

    private FoodDto convertToDtoWithImages(Food food) {
        FoodDto dto = convertToDto(food);
        
        // Get all images
        List<FoodImage> images = foodImageRepository.findByFoodIdOrderByIsPrimaryDescCreatedAtDesc(food.getId());
        List<FoodImageDto> imageDtos = images.stream()
                .map(this::convertImageToDto)
                .collect(Collectors.toList());
        
        dto.setImages(imageDtos);
        
        if (!imageDtos.isEmpty() && imageDtos.get(0).getIsPrimary()) {
            dto.setPrimaryImageUrl(imageDtos.get(0).getImageUrl());
        }
        
        return dto;
    }

    private FoodImageDto convertImageToDto(FoodImage image) {
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