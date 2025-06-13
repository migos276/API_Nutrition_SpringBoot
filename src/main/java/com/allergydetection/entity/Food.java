package com.allergydetection.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name = "foods")
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Food name is required")
    private String name;

    private String category;

    @Column(columnDefinition = "TEXT")
    private String ingredients;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "is_base_food")
    private Boolean isBaseFood = false;

    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Meal> meals;

    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodImage> images;

    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WeeklyPlan> weeklyPlans;

    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BuffetFood> buffetFoods;

    // Constructors
    public Food() {}

    public Food(String name, String category, String ingredients) {
        this.name = name;
        this.category = category;
        this.ingredients = ingredients;
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

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    public List<FoodImage> getImages() {
        return images;
    }

    public void setImages(List<FoodImage> images) {
        this.images = images;
    }

    public List<WeeklyPlan> getWeeklyPlans() {
        return weeklyPlans;
    }

    public void setWeeklyPlans(List<WeeklyPlan> weeklyPlans) {
        this.weeklyPlans = weeklyPlans;
    }

    public List<BuffetFood> getBuffetFoods() {
        return buffetFoods;
    }

    public void setBuffetFoods(List<BuffetFood> buffetFoods) {
        this.buffetFoods = buffetFoods;
    }
}