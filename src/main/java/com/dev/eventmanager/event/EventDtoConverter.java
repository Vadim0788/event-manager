package com.dev.eventmanager.event;

import com.dev.eventmanager.location.LocationDtoConverter;
import com.dev.eventmanager.user.UserDtoConverter;
import org.springframework.stereotype.Component;

@Component
public class EventDtoConverter {
    LocationDtoConverter locationDtoConverter;
    UserDtoConverter userDtoConverter;

    public EventDtoConverter(LocationDtoConverter locationDtoConverter, UserDtoConverter userDtoConverter) {
        this.locationDtoConverter = locationDtoConverter;
        this.userDtoConverter = userDtoConverter;
    }


    public EventDto toDto(Event event) {
        return new EventDto(
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

    public Event toDomain(EventDto eventDto) {
        return new Event(
                eventDto.id(),
                eventDto.name(),
                eventDto.ownerId(),
                eventDto.maxPlaces(),
                eventDto.occupiedPlaces(),
                eventDto.date(),
                eventDto.cost(),
                eventDto.duration(),
                eventDto.locationId(),
                eventDto.status()
        );
    }
}
