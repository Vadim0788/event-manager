package com.dev.eventmanager.event;

import com.dev.eventmanager.location.LocationEntityConverter;
import com.dev.eventmanager.user.UserEntityConverter;
import org.springframework.stereotype.Component;

@Component
public class EventEntityConverter {

    public EventEntityConverter(LocationEntityConverter locationEntityConverter, UserEntityConverter userEntityConverter) {
        this.locationEntityConverter = locationEntityConverter;
        this.userEntityConverter = userEntityConverter;
    }

    LocationEntityConverter locationEntityConverter;
    UserEntityConverter userEntityConverter;


    public EventEntity toEntity(Event event) {
        return new EventEntity(
                event.id(),
                event.name(),
                event.ownerId(),
                event.maxPlaces(),
                event.occupiedPlaces(),
                event.date(),
                event.cost(),
                event.duration(),
                event.locationId(),
                event.status()
        );
    }

    public Event toDomain(EventEntity eventEntity) {
        return new Event(
                eventEntity.getId(),
                eventEntity.getName(),
                eventEntity.getOwnerId(),
                eventEntity.getMaxPlaces(),
                eventEntity.getOccupiedPlaces(),
                eventEntity.getDate(),
                eventEntity.getCost(),
                eventEntity.getDuration(),
                eventEntity.getLocationId(),
                eventEntity.getStatus()
        );
    }
}
