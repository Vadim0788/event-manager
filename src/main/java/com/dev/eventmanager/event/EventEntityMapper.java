package com.dev.eventmanager.event;

import com.dev.eventmanager.location.LocationEntity;
import com.dev.eventmanager.users.UserEntity;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class EventEntityMapper {


    public EventEntity toEntity(Event event, LocationEntity location, UserEntity owner) {

        return new EventEntity(
                null,
                event.name(),
                event.maxPlaces(),
                event.date(),
                event.cost(),
                event.duration(),
                location,
                owner,
                0,
                EventStatus.WAIT_START.name()
        );
    }

    public Event toDomain(EventEntity eventEntity) {
        return new Event(
                eventEntity.getId(),
                eventEntity.getName(),
                eventEntity.getMaxPlaces(),
                eventEntity.getDate(),
                eventEntity.getCost(),
                eventEntity.getDuration(),
                eventEntity.getLocation().getId()
        );
    }
}
