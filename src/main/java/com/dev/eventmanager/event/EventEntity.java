package com.dev.eventmanager.event;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;


import java.math.BigDecimal;

import java.time.OffsetDateTime;


@Entity
@Table(name = "events")
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;


    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "max_places", nullable = false)
    @Min(1)
    private int maxPlaces;

    @Min(0)
    @Column(name = "occupied_pl", nullable = false)
    private int occupiedPlaces;

    @Column(name = "date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime date;

    @Column(name = "cost", nullable = false)
    private BigDecimal cost;

    @Column(name = "duration")
    private int duration;

    @Column(name = "loc_id", nullable = false)
    private Long locationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    public EventEntity() {
    }

    public EventEntity(
            Long id,
            String name,
            Long ownerId,
            int maxPlaces,
            int occupiedPlaces,
            OffsetDateTime date,
            BigDecimal cost,
            int duration,
            Long locationId,
            Status status
    ) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.maxPlaces = maxPlaces;
        this.occupiedPlaces = occupiedPlaces;
        this.date = date;
        this.cost = cost;
        this.duration = duration;
        this.status = status;
        this.locationId = locationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxPlaces() {
        return maxPlaces;
    }

    public void setMaxPlaces(int maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public int getOccupiedPlaces() {
        return occupiedPlaces;
    }

    public void setOccupiedPlaces(int occupiedPlaces) {
        this.occupiedPlaces = occupiedPlaces;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public void addParticipant() {
        if (occupiedPlaces >= maxPlaces) {
            throw new IllegalStateException("No more places available.");
        }
        occupiedPlaces++;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long owner_id) {
        this.ownerId = owner_id;
    }
}
