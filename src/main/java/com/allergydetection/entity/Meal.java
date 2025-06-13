package com.allergydetection.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

@Entity
@Table(name = "meals")
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    @NotNull(message = "Food is required")
    private Food food;

    @Column(name = "meal_time", nullable = false)
    @NotNull(message = "Meal time is required")
    private LocalDateTime mealTime;

    @Column(nullable = false)
    @Positive(message = "Quantity must be positive")
    private Double quantity;

    @Column(columnDefinition = "TEXT")
    private String notes;

    // Constructors
    public Meal() {}

    public Meal(User user, Food food, LocalDateTime mealTime, Double quantity, String notes) {
        this.user = user;
        this.food = food;
        this.mealTime = mealTime;
        this.quantity = quantity;
        this.notes = notes;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
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
}