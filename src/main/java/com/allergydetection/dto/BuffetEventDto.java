package com.allergydetection.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.List;

public class BuffetEventDto {
    private Long id;
    
    @NotBlank(message = "Event name is required")
    private String eventName;
    
    @NotNull(message = "Event date is required")
    private LocalDate eventDate;
    
    @Positive(message = "Estimated guests must be positive")
    private Integer estimatedGuests;
    
    @NotNull(message = "Creator ID is required")
    private Long createdBy;
    
    // Informations supplémentaires
    private String creatorUsername;
    private List<BuffetFoodDto> foods;
    private Integer totalFoods;

    // Constructors
    public BuffetEventDto() {}

    public BuffetEventDto(Long id, String eventName, LocalDate eventDate, Integer estimatedGuests, 
                         Long createdBy, String creatorUsername) {
        this.id = id;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.estimatedGuests = estimatedGuests;
        this.createdBy = createdBy;
        this.creatorUsername = creatorUsername;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public Integer getEstimatedGuests() {
        return estimatedGuests;
    }

    public void setEstimatedGuests(Integer estimatedGuests) {
        this.estimatedGuests = estimatedGuests;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }

    public List<BuffetFoodDto> getFoods() {
        return foods;
    }

    public void setFoods(List<BuffetFoodDto> foods) {
        this.foods = foods;
        this.totalFoods = foods != null ? foods.size() : 0;
    }

    public Integer getTotalFoods() {
        return totalFoods;
    }

    public void setTotalFoods(Integer totalFoods) {
        this.totalFoods = totalFoods;
    }
}