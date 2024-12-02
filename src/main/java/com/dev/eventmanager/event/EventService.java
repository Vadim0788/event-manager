package com.dev.eventmanager.event;

import com.dev.eventmanager.location.LocationRepository;
import com.dev.eventmanager.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

import java.util.Collection;
import java.util.List;

@Service
public class EventService {

    private final LocationRepository locationRepository;

    private final EventRepository eventRepository;

    private final EventEntityConverter entityConverter;

    private final UserRepository userRepository;

    public EventService(LocationRepository locationRepository, EventRepository eventRepository, EventEntityConverter entityConverter, UserRepository userRepository) {
        this.locationRepository = locationRepository;
        this.eventRepository = eventRepository;
        this.entityConverter = entityConverter;
        this.userRepository = userRepository;
    }

    public Event createEvent(Event event) {
        if (eventRepository.existsByName(event.name())) {
            throw new IllegalArgumentException("Event name already taken");
        }

        var location = locationRepository.findById(event.locationId())
                .orElseThrow(() -> new EntityNotFoundException("Location not found for ID: " + event.locationId()));

        if (location.getCapacity() < event.maxPlaces()) {
            throw new IllegalArgumentException("Location does not have enough capacity for the event.");
        }
        var entityToSave = entityConverter.toEntity(event);
        return entityConverter.toDomain(
                eventRepository.save(entityToSave)
        );
    }

    public void deleteLocation(long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EntityNotFoundException("Not found event by id=%s"
                    .formatted(eventId));
        }

        eventRepository.deleteEventById(eventId);
    }

    public Event findById(long eventId) {
        var entity = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Not found event by id=%s".formatted(eventId)));
        return entityConverter.toDomain(entity);
    }

    public Event updateEvent(Long id, Event eventToUpdate) {
        var existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No found event by id=%s"
                                .formatted(id)));

        if (eventRepository.existsByName(eventToUpdate.name())) {
            throw new IllegalArgumentException("Event name already taken");
        }

        if (eventToUpdate.maxPlaces() < existingEvent.getOccupiedPlaces()) {
            throw new IllegalArgumentException("Cannot reduce maxPlaces below the number of already registered participants.");
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
                        eventSearchFilter.durationMax() != null ? eventSearchFilter.durationMax() : Integer.MAX_VALUE,
                        eventSearchFilter.durationMin() != null ? eventSearchFilter.durationMin() : 0,
                        eventSearchFilter.dateStartBefore(),
                        eventSearchFilter.dateStartAfter(),
                        eventSearchFilter.placesMin() != null ? eventSearchFilter.placesMin() : 0,
                        eventSearchFilter.placesMax() != null ? eventSearchFilter.placesMax() : Integer.MAX_VALUE,
                        eventSearchFilter.locationId(),
                        eventSearchFilter.eventStatus(),
                        eventSearchFilter.name(),
                        eventSearchFilter.costMin(),
                        eventSearchFilter.costMax()
                )
                .stream()
                .map(entityConverter::toDomain)
                .toList();
    }

    public void registerForEvent(Long eventId) {
        var event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event with ID " + eventId + " not found"));


        if (event.getStatus() == Status.CANCELLED) {
            throw new IllegalArgumentException("Cannot register for a cancelled event.");
        }
        if (event.getStatus() == Status.FINISHED) {
            throw new IllegalArgumentException("Cannot register for a finished event.");
        }
        if (event.getDate().isBefore(OffsetDateTime.now())) {
            throw new IllegalArgumentException("Cannot register for an event that has already started.");
        }

        if (event.getOccupiedPlaces() >= event.getMaxPlaces()) {
            throw new IllegalArgumentException("No available places for this event.");
        }

        event.setOccupiedPlaces(event.getOccupiedPlaces() + 1);
        eventRepository.save(event);
    }

    public void cancelRegistration(Long eventId) {
        var event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event with ID " + eventId + " not found"));

        if (event.getStatus() == Status.STARTED) {
            throw new IllegalArgumentException("Cannot cancel registration for an event that has already started.");
        }
        if (event.getStatus() == Status.FINISHED) {
            throw new IllegalArgumentException("Cannot cancel registration for an event that has already finished.");
        }

        if (event.getOccupiedPlaces() <= 0) {
            throw new IllegalArgumentException("No registrations to cancel for this event.");
        }

        event.setOccupiedPlaces(event.getOccupiedPlaces() - 1);
        eventRepository.save(event);
    }

    public List<Event> getUserRegisteredEvents(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User with ID " + userId + " not found");
        }

        List<EventEntity> registeredEvents = eventRepository.findByOwnerId(userId);
        return registeredEvents.stream()
                .map(entityConverter::toDomain)
                .toList();
    }
}
