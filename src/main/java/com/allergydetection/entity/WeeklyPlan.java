package com.allergydetection.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "weekly_plans")
public class WeeklyPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;

    @Column(name = "week_start_date", nullable = false)
    @NotNull(message = "Week start date is required")
    private LocalDate weekStartDate;

    @Column(name = "day_of_week", nullable = false)
    @Min(value = 0, message = "Day of week must be between 0 and 6")
    @Max(value = 6, message = "Day of week must be between 0 and 6")
    private Integer dayOfWeek;

    @Column(name = "meal_type", nullable = false)
    @NotBlank(message = "Meal type is required")
    private String mealType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    @NotNull(message = "Food is required")
    private Food food;

    @Column(name = "planned_quantity")
    private Double plannedQuantity = 1.0;

    // Constructors
    public WeeklyPlan() {}

    public WeeklyPlan(User user, LocalDate weekStartDate, Integer dayOfWeek, String mealType, Food food, Double plannedQuantity) {
        this.user = user;
        this.weekStartDate = weekStartDate;
        this.dayOfWeek = dayOfWeek;
        this.mealType = mealType;
        this.food = food;
        this.plannedQuantity = plannedQuantity;
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
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public Double getPlannedQuantity() {
        return plannedQuantity;
    }

    public void setPlannedQuantity(Double plannedQuantity) {
        this.plannedQuantity = plannedQuantity;
    }
}