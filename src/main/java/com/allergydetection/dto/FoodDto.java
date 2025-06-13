package com.allergydetection.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class FoodDto {
    private Long id;
    
    @NotBlank(message = "Food name is required")
    private String name;
    
    private String category;
    private String ingredients;
    private String imagePath;
    private Boolean isBaseFood;
    private String imageUrl;
    private List<FoodImageDto> images;
    private String primaryImageUrl;

    // Constructors
    public FoodDto() {}

    public FoodDto(Long id, String name, String category, String ingredients, String imagePath, Boolean isBaseFood) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.ingredients = ingredients;
        this.imagePath = imagePath;
        this.isBaseFood = isBaseFood;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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

    public List<FoodImageDto> getImages() {
        return images;
    }

    public void setImages(List<FoodImageDto> images) {
        this.images = images;
    }

    public String getPrimaryImageUrl() {
        return primaryImageUrl;
    }

    public void setPrimaryImageUrl(String primaryImageUrl) {
        this.primaryImageUrl = primaryImageUrl;
    }
}