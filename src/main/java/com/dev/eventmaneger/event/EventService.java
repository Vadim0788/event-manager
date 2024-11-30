package com.dev.eventmaneger.event;

import com.dev.eventmaneger.location.Location;
import com.dev.eventmaneger.location.LocationEntityConverter;
import com.dev.eventmaneger.location.LocationRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

@Service
public class EventService {

    private final EventRepository eventRepository;

    private final EventEntityConverter entityConverter;

    public EventService(EventRepository eventRepository, EventEntityConverter entityConverter) {
        this.eventRepository = eventRepository;
        this.entityConverter = entityConverter;
    }

    public Event createEvent(Event event) {
        if (eventRepository.existsByName(event.name())) {
            throw new IllegalArgumentException("Event name already taken");
        }

        var entityToSave = entityConverter.toEntity(event);
        return entityConverter.toDomain(
                eventRepository.save(entityToSave)
        );
    }

    public void deleteLocation(long locationId) {
        if (!eventRepository.existsById(locationId)) {
            throw new EntityNotFoundException("Not found event by id=%s"
                    .formatted(locationId));
        }

        eventRepository.deleteById(locationId);
    }

    public Event findById(long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EntityNotFoundException("Not found event by id=%s"
                    .formatted(eventId));
        }

        var entity = eventRepository.getReferenceById(eventId);
        return entityConverter.toDomain(entity);
    }

    public Event updateEvent(Long id, Event eventToUpdate) {
        if (!eventRepository.existsById(id))
            throw new EntityNotFoundException(
                    "No found event by id=%s"
                            .formatted(id));
        if (eventRepository.existsByName(eventToUpdate.name())) {
            throw new IllegalArgumentException("Event name already taken");
        }

        eventRepository.updateEvent(
                id,
                eventToUpdate.name(),
                eventToUpdate.maxPlaces(),
                eventToUpdate.date(),
                eventToUpdate.cost(),
                eventToUpdate.duration(),
                eventToUpdate.locationId()
        );
        return entityConverter.toDomain(
                eventRepository.findById(id).orElseThrow()
        );


    }

    public Collection<Event> searchAllEvent(@Valid EventSearchFilter eventSearchFilter) {

        return eventRepository.searchEvents(
                        eventSearchFilter.durationMax(),
                        eventSearchFilter.dateStartBefore(),
                        eventSearchFilter.placesMin(),
                        eventSearchFilter.locationId(),
                        eventSearchFilter.eventStatus(),
                        eventSearchFilter.name(),
                        eventSearchFilter.placesMax(),
                        eventSearchFilter.costMin(),
                        eventSearchFilter.dateStartAfter(),
                        eventSearchFilter.costMax(),
                        eventSearchFilter.durationMin()
                )
                .stream()
                .map(entityConverter::toDomain)
                .toList();
    }
}
