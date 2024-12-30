package com.dev.eventmanager.event;

import com.dev.eventmanager.location.LocationRepository;
import com.dev.eventmanager.users.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import com.dev.eventmanager.location.LocationEntity;

import java.util.List;


@Service
public class EventService {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final EventEntityMapper eventEntityMapper;
    private final UserService userService;
    private final EventDtoMapper eventDtoMapper;


    public EventService(EventRepository eventRepository, LocationRepository locationRepository, EventEntityMapper eventEntityMapper, UserService userService, EventDtoMapper eventDtoMapper) {

        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
        this.eventEntityMapper = eventEntityMapper;
        this.userService = userService;
        this.eventDtoMapper = eventDtoMapper;
    }

    @Transactional
    public EventDto createEvent(Event event) {

        LocationEntity location = locationRepository.findById(event.locationId())
                .orElseThrow(() -> new EntityNotFoundException("Location not found"));

        if (event.maxPlaces() > location.getCapacity()) {
            throw new IllegalArgumentException("Location capacity exceeded");
        }

        if (eventRepository.existsByName(event.name())) {
            throw new IllegalArgumentException("Event name already taken");
        }

        UserEntity userEntity = userService.getAuthenticatedUserEntity();

        var entityToSave = eventEntityMapper.toEntity(event, location, userEntity);


        event = eventEntityMapper.toDomain(
                eventRepository.save(entityToSave)
        );
        return eventDtoMapper.toDto(event);

    }

    @Transactional
    public void deleteEvent(long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EntityNotFoundException("Not found event by id=%s"
                    .formatted(eventId));
        }

        eventRepository.deleteById(eventId);
    }

    public EventDto findById(long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EntityNotFoundException("Not found event by id=%s"
                    .formatted(eventId));
        }

        var entity = eventRepository.getReferenceById(eventId);
        var event = eventEntityMapper.toDomain(entity);
        return eventDtoMapper.toDto(event);
    }

    @Transactional
    public EventDto updateEvent(Long id, Event eventToUpdate) {
        if (!eventRepository.existsById(id))
            throw new EntityNotFoundException(
                    "No found event by id=%s"
                            .formatted(id));
        if (eventRepository.existsByName(eventToUpdate.name())) {
            throw new IllegalArgumentException("Event name already taken");
        }

        LocationEntity location = locationRepository.findById(eventToUpdate.locationId())
                .orElseThrow(() -> new EntityNotFoundException("Location not found"));

        eventRepository.updateEvent(
                id,
                eventToUpdate.name(),
                eventToUpdate.maxPlaces(),
                eventToUpdate.date(),
                eventToUpdate.cost(),
                eventToUpdate.duration(),
                location
        );

        Event event = eventEntityMapper.toDomain(
                eventRepository.findById(id).orElseThrow()
        );
        return eventDtoMapper.toDto(event);
    }

    public List<EventDto> search(@Valid EventFilterRequest eventFilterRequest) {
        List<EventEntity> listEventEntities = eventRepository.filterEvents(
                eventFilterRequest.name(),
                eventFilterRequest.durationMin(),
                eventFilterRequest.durationMax(),
                eventFilterRequest.dateStartAfter(),
                eventFilterRequest.dateStartBefore(),
                eventFilterRequest.placesMin(),
                eventFilterRequest.placesMax(),
                eventFilterRequest.locationId(),
                eventFilterRequest.eventStatus(),
                eventFilterRequest.costMin(),
                eventFilterRequest.costMax()
        );
        List<Event> eventList = listEventEntities.stream()
                .map(eventEntityMapper::toDomain).toList();

        return eventList.stream()
                .map(eventDtoMapper::toDto).toList();
    }

    public List<Event> getMyEvents() {
        UserEntity userEntity = userService.getAuthenticatedUserEntity();
        List<EventEntity> listEventEntities = eventRepository.getEventEntitiesByOwner(userEntity);
        return listEventEntities.stream().map(eventEntityMapper::toDomain).toList();

    }

}
