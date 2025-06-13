package com.allergydetection.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class WeeklyPlanDto {
    private Long id;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Week start date is required")
    private LocalDate weekStartDate;
    
    @Min(value = 0, message = "Day of week must be between 0 and 6")
    @Max(value = 6, message = "Day of week must be between 0 and 6")
    private Integer dayOfWeek;
    
    @NotBlank(message = "Meal type is required")
    private String mealType;
    
    @NotNull(message = "Food ID is required")
    private Long foodId;
    
    @Positive(message = "Planned quantity must be positive")
    private Double plannedQuantity;
    
    // Informations supplémentaires pour l'affichage
    private String foodName;
    private String foodCategory;
    private String foodIngredients;
    private String dayName;

    // Constructors
    public WeeklyPlanDto() {}

    public WeeklyPlanDto(Long id, Long userId, LocalDate weekStartDate, Integer dayOfWeek, 
                        String mealType, Long foodId, Double plannedQuantity, 
                        String foodName, String foodCategory, String foodIngredients) {
        this.id = id;
        this.userId = userId;
        this.weekStartDate = weekStartDate;
        this.dayOfWeek = dayOfWeek;
        this.mealType = mealType;
        this.foodId = foodId;
        this.plannedQuantity = plannedQuantity;
        this.foodName = foodName;
        this.foodCategory = foodCategory;
        this.foodIngredients = foodIngredients;
        this.dayName = getDayNameFromNumber(dayOfWeek);
    }

    private String getDayNameFromNumber(Integer dayOfWeek) {
        if (dayOfWeek == null) return null;
        String[] days = {"Dimanche", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"};
        return dayOfWeek >= 0 && dayOfWeek < days.length ? days[dayOfWeek] : null;
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

    public LocalDate getWeekStartDate() {
        return weekStartDate;
    }

    public void setWeekStartDate(LocalDate weekStartDate) {
        this.weekStartDate = weekStartDate;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        this.dayName = getDayNameFromNumber(dayOfWeek);
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public Long getFoodId() {
        return foodId;
    }

    public void setFoodId(Long foodId) {
        this.foodId = foodId;
    }

    public Double getPlannedQuantity() {
        return plannedQuantity;
    }

    public void setPlannedQuantity(Double plannedQuantity) {
        this.plannedQuantity = plannedQuantity;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(String foodCategory) {
        this.foodCategory = foodCategory;
    }

    public String getFoodIngredients() {
        return foodIngredients;
    }

    public void setFoodIngredients(String foodIngredients) {
        this.foodIngredients = foodIngredients;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }
}