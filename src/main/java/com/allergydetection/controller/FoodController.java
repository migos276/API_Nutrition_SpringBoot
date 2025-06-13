package com.allergydetection.controller;

import com.allergydetection.dto.FoodDto;
import com.allergydetection.service.FoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/foods")
@Tag(name = "Food Management", description = "APIs for managing foods")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @PostMapping
    @Operation(summary = "Create a new food")
    public ResponseEntity<Map<String, Object>> createFood(@Valid @RequestBody CreateFoodRequest request) {
        FoodDto foodDto = new FoodDto();
        foodDto.setName(request.getName());
        foodDto.setCategory(request.getCategory());
        foodDto.setIngredients(request.getIngredients());
        foodDto.setIsBaseFood(request.getIsBaseFood());

        FoodDto createdFood;
        if (request.getImageUrl() != null && !request.getImageUrl().trim().isEmpty()) {
            createdFood = foodService.createFoodWithImageUrl(foodDto, request.getImageUrl());
        } else {
            createdFood = foodService.createFood(foodDto);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("food_id", createdFood.getId());
        response.put("message", "Aliment créé avec succès");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all foods")
    public ResponseEntity<Map<String, Object>> getAllFoods() {
        List<FoodDto> foods = foodService.getAllFoods();
        
        Map<String, Object> response = new HashMap<>();
        response.put("foods", foods);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get food by ID with images")
    public ResponseEntity<FoodDto> getFoodById(@PathVariable Long id) {
        FoodDto food = foodService.getFoodById(id);
        return ResponseEntity.ok(food);
    }

    @GetMapping("/search")
    @Operation(summary = "Search foods")
    public ResponseEntity<Map<String, Object>> searchFoods(@RequestParam String q) {
        List<FoodDto> foods = foodService.searchFoods(q);
        
        Map<String, Object> response = new HashMap<>();
        response.put("foods", foods);
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update food")
    public ResponseEntity<Map<String, Object>> updateFood(@PathVariable Long id, @Valid @RequestBody FoodDto foodDto) {
        FoodDto updatedFood = foodService.updateFood(id, foodDto);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Aliment mis à jour avec succès");
        response.put("food", updatedFood);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete food")
    public ResponseEntity<Map<String, Object>> deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Aliment supprimé avec succès");
        
        return ResponseEntity.ok(response);
    }

    // Inner class for request DTO
    public static class CreateFoodRequest {
        @Valid
        private String name;
        private String category;
        private String ingredients;
        private Boolean isBaseFood;
        private String imageUrl;

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getIngredients() {
            return ingredients;
        }

        public void setIngredients(String ingredients) {
            this.ingredients = ingredients;
        }

        public Boolean getIsBaseFood() {
            return isBaseFood;
        }

        public void setIsBaseFood(Boolean isBaseFood) {
            this.isBaseFood = isBaseFood;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}