package com.dev.eventmanager.event;

import com.dev.eventmanager.location.LocationEntity;
import com.dev.eventmanager.users.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;


@Entity
@Table(name = "events")
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "max_places", nullable = false)
    private Long maxPlaces;
    @Column(name = "date", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime date;
    @Column(name = "cost", nullable = false)
    @DecimalMin("0.0")
    private BigDecimal cost;
    @Column(name = "duration", nullable = false)
    private Integer duration;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private LocationEntity location;
    @JoinColumn(name = "owner_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity owner;
    @Column(name = "occupied_places", nullable = false)
    @ColumnDefault("0")
    private Integer occupiedPlaces;
    @Column(name = "status", nullable = false)
    private String status;
    @Column(name = "timezone_offset", nullable = false)
    private String timezoneOffset;

    public EventEntity() {
    }

    public EventEntity(
            Long id,
            String name,
            Long maxPlaces,
            OffsetDateTime date,
            BigDecimal cost,
            Integer duration,
            LocationEntity location,
            UserEntity owner,
            Integer occupiedPlaces,
            String status,
            String timezoneOffset
    ) {
        this.id = id;
        this.name = name;
        this.maxPlaces = maxPlaces;
        this.date = date;
        this.cost = cost;
        this.duration = duration;
        this.location = location;
        this.owner = owner;
        this.occupiedPlaces = occupiedPlaces;
        this.status = status;
        this.timezoneOffset = timezoneOffset;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwnerId(UserEntity owner) {
        this.owner = owner;
    }

    public Long getMaxPlaces() {
        return maxPlaces;
    }

    public void setMaxPlaces(Long maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public Integer getOccupiedPlaces() {
        return occupiedPlaces;
    }

    public void setOccupiedPlaces(Integer occupiedPlaces) {
        this.occupiedPlaces = occupiedPlaces;
    }

    public OffsetDateTime getDate() {
        return date.withOffsetSameInstant(ZoneOffset.of(getTimezoneOffset()));
    }

    public void setDate(OffsetDateTime date) {

        this.date = date.withOffsetSameInstant(ZoneOffset.UTC);
        this.timezoneOffset = date.getOffset().toString();
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocationEntity getLocation() {
        return location;
    }

    public void setLocation(LocationEntity location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Transient
    public OffsetDateTime getEndTime() {
        return date.plus(duration, ChronoUnit.MINUTES);
    }

    public String getTimezoneOffset() {
        return timezoneOffset;
    }

    public void setTimezoneOffset(String timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }
}
