package com.allergydetection.controller;

import com.allergydetection.dto.MealDto;
import com.allergydetection.service.MealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Meal Management", description = "APIs for managing meals")
public class MealController {

    @Autowired
    private MealService mealService;

    @PostMapping("/meals")
    @Operation(summary = "Create a new meal")
    public ResponseEntity<Map<String, Object>> createMeal(@Valid @RequestBody MealDto mealDto) {
        MealDto createdMeal = mealService.createMeal(mealDto);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("meal_id", createdMeal.getId());
        response.put("message", "Repas enregistré avec succès");
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}/meals")
    @Operation(summary = "Get user meals with optional date filtering")
    public ResponseEntity<Map<String, Object>> getUserMeals(
            @PathVariable Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start_date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end_date) {
        
        List<MealDto> meals;
        if (start_date != null && end_date != null) {
            meals = mealService.getUserMeals(userId, start_date, end_date);
        } else {
            meals = mealService.getUserMeals(userId);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("meals", meals);
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/meals/{id}")
    @Operation(summary = "Update meal")
    public ResponseEntity<Map<String, Object>> updateMeal(@PathVariable Long id, @Valid @RequestBody MealDto mealDto) {
        MealDto updatedMeal = mealService.updateMeal(id, mealDto);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Repas mis à jour avec succès");
        response.put("meal", updatedMeal);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/meals/{id}")
    @Operation(summary = "Delete meal")
    public ResponseEntity<Map<String, Object>> deleteMeal(@PathVariable Long id) {
        mealService.deleteMeal(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Repas supprimé avec succès");
        
        return ResponseEntity.ok(response);
    }
}