package com.allergydetection.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "buffet_foods")
public class BuffetFood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buffet_id", nullable = false)
    @NotNull(message = "Buffet event is required")
    private BuffetEvent buffetEvent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    @NotNull(message = "Food is required")
    private Food food;

    @Column(name = "planned_quantity")
    @Positive(message = "Planned quantity must be positive")
    private Double plannedQuantity = 1.0;

    @Column(nullable = false)
    private String unit = "portions";

    // Constructors
    public BuffetFood() {}

    public BuffetFood(BuffetEvent buffetEvent, Food food, Double plannedQuantity, String unit) {
        this.buffetEvent = buffetEvent;
        this.food = food;
        this.plannedQuantity = plannedQuantity;
        this.unit = unit;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BuffetEvent getBuffetEvent() {
        return buffetEvent;
    }

    public void setBuffetEvent(BuffetEvent buffetEvent) {
        this.buffetEvent = buffetEvent;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}