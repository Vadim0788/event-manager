package com.dev.eventmaneger.event;

import com.dev.eventmaneger.location.*;
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

    private final EventService eventService ;

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
        Event createdEvent = eventService.createEvent(
                dtoConverter.toDomain(eventToCreate)
        );
        return status(HttpStatus.CREATED)
                .body(dtoConverter.toDto(createdEvent));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable("id") long locationId
    ){
        log.info("Get request for delete event: event = {}", locationId);
        eventService.deleteLocation(locationId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{id}")
    public EventDto findById(
            @PathVariable("id") long eventId
    ){
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
            @Valid EventSearchFilter eventSearchFilter
    ) {
        log.info("Get request for getAllEvents");
        return eventService.searchAllEvent(eventSearchFilter)
                .stream()
                .map(dtoConverter::toDto)
                .toList();
    }

}
