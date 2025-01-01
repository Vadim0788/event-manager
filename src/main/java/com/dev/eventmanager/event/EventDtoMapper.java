package com.dev.eventmanager.event;


import org.springframework.stereotype.Component;

@Component
public class EventDtoMapper {
    public EventDto toDto(Event event) {
        return new EventDto(
                event.id(),
                event.name(),
                event.maxPlaces(),
                event.date(),
                event.cost(),
                event.duration(),
                event.locationId()
        );
    }

    public Event toDomain(EventDto eventDto) {
        return new Event(
                eventDto.id(),
                eventDto.name(),
                eventDto.maxPlaces(),
                eventDto.date(),
                eventDto.cost(),
                eventDto.duration(),
                eventDto.locationId()
        );
    }
}


