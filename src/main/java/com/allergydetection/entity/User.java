package com.allergydetection.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Username is required")
    private String username;

    @Column(unique = true, nullable = false)
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Meal> meals;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Symptom> symptoms;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WeeklyPlan> weeklyPlans;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BuffetEvent> buffetEvents;

    // Constructors
    public User() {}

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    public List<Symptom> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(List<Symptom> symptoms) {
        this.symptoms = symptoms;
    }

    public List<WeeklyPlan> getWeeklyPlans() {
        return weeklyPlans;
    }

    public void setWeeklyPlans(List<WeeklyPlan> weeklyPlans) {
        this.weeklyPlans = weeklyPlans;
    }

    public List<BuffetEvent> getBuffetEvents() {
        return buffetEvents;
    }

    public void setBuffetEvents(List<BuffetEvent> buffetEvents) {
        this.buffetEvents = buffetEvents;
    }
}