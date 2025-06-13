package com.allergydetection.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "buffet_events")
public class BuffetEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_name", nullable = false)
    @NotBlank(message = "Event name is required")
    private String eventName;

    @Column(name = "event_date", nullable = false)
    @NotNull(message = "Event date is required")
    private LocalDate eventDate;

    @Column(name = "estimated_guests", nullable = false)
    @Positive(message = "Estimated guests must be positive")
    private Integer estimatedGuests;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    @NotNull(message = "Creator is required")
    private User createdBy;

    @OneToMany(mappedBy = "buffetEvent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BuffetFood> buffetFoods;

    // Constructors
    public BuffetEvent() {}

    public BuffetEvent(String eventName, LocalDate eventDate, Integer estimatedGuests, User createdBy) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.estimatedGuests = estimatedGuests;
        this.createdBy = createdBy;
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

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public List<BuffetFood> getBuffetFoods() {
        return buffetFoods;
    }

    public void setBuffetFoods(List<BuffetFood> buffetFoods) {
        this.buffetFoods = buffetFoods;
    }
}