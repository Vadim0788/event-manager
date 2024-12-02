package com.dev.eventmanager.event;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/events")
public class EventController {

    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    private final EventService eventService;

    private final EventDtoConverter dtoConverter;

    public EventController(EventService eventService, EventDtoConverter dtoConverter) {
        this.eventService = eventService;
        this.dtoConverter = dtoConverter;
    }

    @PostMapping
    public ResponseEntity<EventDto> creatEvent(
            @RequestBody @Valid EventDto eventToCreate
    ) {
        log.info("Get request for create event: event {}", eventToCreate);
        var eventWithDefaultStatus = eventToCreate.status() == null
                ? new EventDto(
                eventToCreate.id(),
                eventToCreate.name(),
                eventToCreate.ownerId(),
                eventToCreate.maxPlaces(),
                eventToCreate.occupiedPlaces(),
                eventToCreate.date(),
                eventToCreate.cost(),
                eventToCreate.duration(),
                eventToCreate.locationId(),
                Status.WAIT_START
        ) : eventToCreate;
        Event createdEvent = eventService.createEvent(
                dtoConverter.toDomain(eventWithDefaultStatus)
        );
        return status(HttpStatus.CREATED)
                .body(dtoConverter.toDto(createdEvent));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable("id") long eventId
    ) {
        log.info("Get request for delete event: event = {}", eventId);
        eventService.deleteLocation(eventId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{id}")
    public EventDto findById(
            @PathVariable("id") long eventId
    ) {
        log.info("Get request for get event: event = {}", eventId);
        var foundEvent = eventService.findById(eventId);

        return dtoConverter.toDto(foundEvent);
    }

    @PutMapping("/{id}")
    public EventDto updateEvent(
            @PathVariable("id") Long id,
            @RequestBody @Valid EventDto eventToUpdate
    ) {
        log.info("Get request for put event by id: id={}, eventToUpdate={}", id, eventToUpdate);
        var updatedLocation = eventService.updateEvent(
                id,
                dtoConverter.toDomain(eventToUpdate));
        return dtoConverter.toDto(updatedLocation);
    }

    @PostMapping("/search")
    public List<EventDto> getAllEvents(
            @RequestBody @Valid EventSearchFilter eventSearchFilter
    ) {
        log.info("Get request for getAllEvents with filters: {}", eventSearchFilter);
        return eventService.searchAllEvent(eventSearchFilter)
                .stream()
                .map(dtoConverter::toDto)
                .toList();
    }

    @PostMapping("/registrations/{eventId}) ")
    public ResponseEntity<String> registerForEvent(@PathVariable("eventId") Long eventId) {
        log.info("Request to register for event with ID: {}", eventId);
        eventService.registerForEvent(eventId);
        return ResponseEntity.ok("Successfully registered for the event!");
    }

    @DeleteMapping("/registrations/cancel/{eventId}")

    public ResponseEntity<Void> cancelRegistration(@PathVariable("eventId") Long eventId) {
        log.info("Request to cancel registration for event with ID: {}", eventId);
        eventService.cancelRegistration(eventId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/registrations/{userId}")
    public ResponseEntity<List<EventDto>> getUserRegisteredEvents(@PathVariable("userId") Long userId) {
        log.info("Request to get registered events for user with ID: {}", userId);
        List<Event> registeredEvents = eventService.getUserRegisteredEvents(userId);
        List<EventDto> response = registeredEvents.stream()
                .map(dtoConverter::toDto)
                .toList();
        return ResponseEntity.ok(response);
    }
}
