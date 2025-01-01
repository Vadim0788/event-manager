package com.dev.eventmanager.event;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/events")
public class EventController {
    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    private final EventService eventService;
    private final EventDtoMapper eventDtoMapper;

    public EventController(EventService eventService, EventDtoMapper eventDtoMapper) {
        this.eventService = eventService;
        this.eventDtoMapper = eventDtoMapper;
    }

    @PostMapping
    public ResponseEntity<EventResponceDto> createEvent(
            @RequestBody @Valid EventDto eventToCreate
    ) {
        log.info("Get request for create event: event = {}", eventToCreate);

        EventResponceDto eventResponceDto = eventService.createEvent(
                eventDtoMapper.toDomain(eventToCreate)
        );
        return status(HttpStatus.CREATED)
                .body(eventResponceDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable("id") long eventId
    ) {
        log.info("Get request for delete event: event = = {}", eventId);

        eventService.deleteEvent(eventId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> findById(
            @PathVariable("id") long eventId
    ) {
        log.info("Get request for get event: event = {}", eventId);
        var foundEventDto = eventService.findById(eventId);

        return ResponseEntity.ok(foundEventDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDto> updateEvent(
            @PathVariable("id") Long id,
            @RequestBody @Valid EventDto eventToUpdate
    ) throws AccessDeniedException {
        log.info("Get request for put event by id: id={}, eventToUpdate={}", id, eventToUpdate);
        var updatedEventDto = eventService.updateEvent(
                id,
                eventDtoMapper.toDomain(eventToUpdate));

        return ResponseEntity.ok(updatedEventDto);
    }

    @PostMapping("/search")
    public ResponseEntity<List<EventDto>> updateEvent(
            @RequestBody @Valid EventFilterRequest eventFilterRequest
    ) {
        log.info("Get request for search event : filer = {}", eventFilterRequest);
        List<EventDto> listEventsDto = eventService.search(
                eventFilterRequest
        );

        return ResponseEntity.ok(listEventsDto);
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> getMyEvents() {

        log.info("Get request for get event for the current user.");
        List<Event> listEvents = eventService.getMyEvents();

        return ResponseEntity.ok(listEvents.stream().map(eventDtoMapper::toDto).toList());
    }
}
