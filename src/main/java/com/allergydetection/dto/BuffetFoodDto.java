package com.allergydetection.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class BuffetFoodDto {
    private Long id;
    
    @NotNull(message = "Buffet ID is required")
    private Long buffetId;
    
    @NotNull(message = "Food ID is required")
    private Long foodId;
    
    @Positive(message = "Planned quantity must be positive")
    private Double plannedQuantity;
    
    private String unit;
    
    // Informations supplémentaires
    private String foodName;
    private String foodCategory;
    private String foodIngredients;
    private Double recommendedQuantity;
    private Double quantityPerPerson;

    // Constructors
    public BuffetFoodDto() {}

    public BuffetFoodDto(Long id, Long buffetId, Long foodId, Double plannedQuantity, String unit,
                        String foodName, String foodCategory, String foodIngredients) {
        this.id = id;
        this.buffetId = buffetId;
        this.foodId = foodId;
        this.plannedQuantity = plannedQuantity;
        this.unit = unit;
        this.foodName = foodName;
        this.foodCategory = foodCategory;
        this.foodIngredients = foodIngredients;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBuffetId() {
        return buffetId;
    }

    public void setBuffetId(Long buffetId) {
        this.buffetId = buffetId;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    public Double getRecommendedQuantity() {
        return recommendedQuantity;
    }

    public void setRecommendedQuantity(Double recommendedQuantity) {
        this.recommendedQuantity = recommendedQuantity;
    }

    public Double getQuantityPerPerson() {
        return quantityPerPerson;
    }

    public void setQuantityPerPerson(Double quantityPerPerson) {
        this.quantityPerPerson = quantityPerPerson;
    }
}