package com.allergydetection.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class MealDto {
    private Long id;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Food ID is required")
    private Long foodId;
    
    @NotNull(message = "Meal time is required")
    private LocalDateTime mealTime;
    
    @Positive(message = "Quantity must be positive")
    private Double quantity;
    
    private String notes;
    private String foodName;
    private String ingredients;

    // Constructors
    public MealDto() {}

    public MealDto(Long id, Long userId, Long foodId, LocalDateTime mealTime, Double quantity, String notes, String foodName, String ingredients) {
        this.id = id;
        this.userId = userId;
        this.foodId = foodId;
        this.mealTime = mealTime;
        this.quantity = quantity;
        this.notes = notes;
        this.foodName = foodName;
        this.ingredients = ingredients;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFoodId() {
        return foodId;
    }

    public void setFoodId(Long foodId) {
        this.foodId = foodId;
    }

    public LocalDateTime getMealTime() {
        return mealTime;
    }

    public void setMealTime(LocalDateTime mealTime) {
        this.mealTime = mealTime;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
}