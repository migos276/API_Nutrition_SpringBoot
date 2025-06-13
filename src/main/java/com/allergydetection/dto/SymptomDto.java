package com.allergydetection.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class SymptomDto {
    private Long id;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotBlank(message = "Symptom type is required")
    private String symptomType;
    
    @Min(value = 1, message = "Severity must be between 1 and 5")
    @Max(value = 5, message = "Severity must be between 1 and 5")
    private Integer severity;
    
    @NotNull(message = "Occurrence time is required")
    private LocalDateTime occurrenceTime;
    
    private String description;

    // Constructors
    public SymptomDto() {}

    public SymptomDto(Long id, Long userId, String symptomType, Integer severity, LocalDateTime occurrenceTime, String description) {
        this.id = id;
        this.userId = userId;
        this.symptomType = symptomType;
        this.severity = severity;
        this.occurrenceTime = occurrenceTime;
        this.description = description;
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

    public String getSymptomType() {
        return symptomType;
    }

    public void setSymptomType(String symptomType) {
        this.symptomType = symptomType;
    }

    public Integer getSeverity() {
        return severity;
    }

    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    public LocalDateTime getOccurrenceTime() {
        return occurrenceTime;
    }

    public void setOccurrenceTime(LocalDateTime occurrenceTime) {
        this.occurrenceTime = occurrenceTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}