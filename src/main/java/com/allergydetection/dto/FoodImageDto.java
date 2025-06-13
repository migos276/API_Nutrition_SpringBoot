package com.allergydetection.dto;

import java.time.LocalDateTime;

public class FoodImageDto {
    private Long id;
    private String filePath;
    private String imageUrl;
    private String originalUrl;
    private Boolean isPrimary;
    private Long fileSize;
    private LocalDateTime createdAt;

    // Constructors
    public FoodImageDto() {}

    public FoodImageDto(Long id, String filePath, String imageUrl, String originalUrl, Boolean isPrimary, Long fileSize, LocalDateTime createdAt) {
        this.id = id;
        this.filePath = filePath;
        this.imageUrl = imageUrl;
        this.originalUrl = originalUrl;
        this.isPrimary = isPrimary;
        this.fileSize = fileSize;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}