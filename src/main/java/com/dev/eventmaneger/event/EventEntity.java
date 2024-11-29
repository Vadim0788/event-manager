package com.dev.eventmaneger.event;

import com.dev.eventmaneger.user.UserEntity;
import com.dev.eventmaneger.location.LocationEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@NamedEntityGraph(
        name = "event-with-locations-and-owners",
        attributeNodes = {
        @NamedAttributeNode("location"),
                @NamedAttributeNode("owner")
}
)
@Table(name = "events")
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "owner_id", insertable = false, updatable = false) // Эта колонка будет только читаться
    private Long ownerId;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false) // Связывает ownerId с UserEntity
    private UserEntity owner;

    @Column(name = "max_places", nullable = false)
    private int maxPlaces;

    @Column(name = "occupied_pl", nullable = false)
    private int occupiedPlaces;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "cost", nullable = false)
    private BigDecimal cost;

    @Column(name = "duration")
    private int duration;

    @Column(name = "loc_id", insertable = false, updatable = false) // Эта колонка будет только читаться
    private Long locationId;

    @ManyToOne
    @JoinColumn(name = "loc_id", nullable = false) // Связывает locationId с LocationEntity
    private LocationEntity location;


    @Column(name = "status", nullable = false)
    private Status status;

    public EventEntity() {
    }

    public EventEntity(Long id, String name, Long ownerId, int maxPlaces, int occupiedPlaces, Date date, BigDecimal cost, int duration, Long locationId, Status status) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.maxPlaces = maxPlaces;
        this.occupiedPlaces = occupiedPlaces;
        this.date = date;
        this.cost = cost;
        this.duration = duration;
        this.locationId = locationId;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
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
}
