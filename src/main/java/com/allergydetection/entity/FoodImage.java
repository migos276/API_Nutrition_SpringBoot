package com.allergydetection.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "food_images")
public class FoodImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "original_url")
    private String originalUrl;

    @Column(name = "is_primary")
    private Boolean isPrimary = false;

    @Column(name = "file_size")
    private Long fileSize = 0L;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Constructors
    public FoodImage() {}

    public FoodImage(Food food, String filePath, String originalUrl, Boolean isPrimary, Long fileSize) {
        this.food = food;
        this.filePath = filePath;
        this.originalUrl = originalUrl;
        this.isPrimary = isPrimary;
        this.fileSize = fileSize;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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