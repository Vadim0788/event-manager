package com.dev.eventmanager.event;

import com.dev.eventmanager.kafkaEvent.EventKafkaMessage;
import com.dev.eventmanager.kafkaEvent.EventMessageSender;
import com.dev.eventmanager.kafkaEvent.FieldChange;
import com.dev.eventmanager.location.LocationRepository;
import com.dev.eventmanager.registration.RegistrationRepository;
import com.dev.eventmanager.users.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import com.dev.eventmanager.location.LocationEntity;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class EventService {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final EventEntityMapper eventEntityMapper;
    private final UserService userService;
    private final EventDtoMapper eventDtoMapper;
    private final EventEntityEventResponseMapper eventEntityEventResponseMapper;
    private final RegistrationRepository registrationRepository;
    private final EventMessageSender eventMessageSender;

    public EventService(EventRepository eventRepository, LocationRepository locationRepository, EventEntityMapper eventEntityMapper, UserService userService, EventDtoMapper eventDtoMapper, EventEntityEventResponseMapper eventEntityEventResponseMapper, RegistrationRepository registrationRepository, EventMessageSender eventMessageSender) {

        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
        this.eventEntityMapper = eventEntityMapper;
        this.userService = userService;
        this.eventDtoMapper = eventDtoMapper;
        this.eventEntityEventResponseMapper = eventEntityEventResponseMapper;
        this.registrationRepository = registrationRepository;
        this.eventMessageSender = eventMessageSender;
    }

    public EventResponseDto createEvent(Event event) {

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


        var eventEntity = eventRepository.save(entityToSave);

        return eventEntityEventResponseMapper.toResponse(eventEntity);

    }


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
    public EventResponseDto updateEvent(Long id, EventUpdateRequestDto updateRequest) {

        if (!eventRepository.existsById(id))
            throw new EntityNotFoundException(
                    "No found event by id=%s"
                            .formatted(id));

        if (eventRepository.existsByName(updateRequest.name())) {
            throw new IllegalArgumentException("Event name already taken");
        }

        UserEntity userEntity = userService.getAuthenticatedUserEntity();
        EventEntity eventEntity = eventRepository.findById(id).orElseThrow();
        LocationEntity locationEntity = locationRepository.findById(eventEntity.getLocation().getId()).orElseThrow();

        if (!eventEntity.getStatus().equals(EventStatus.WAIT_START.name())) {
            throw new IllegalArgumentException("Cannot modify event in status: %s"
                    .formatted(eventEntity.getStatus()));
        }

        if (userEntity.getRole().equals(UserRole.USER.name())
                && eventEntity.getOwner() != userEntity) {
            throw new AccessDeniedException("You are not allowed to update this event");
        }

        if (updateRequest.maxPlaces() < eventEntity.getOccupiedPlaces()) {
            throw new IllegalArgumentException("Invalid event data: maxPlaces(%s) must be greater than the number of registered users (%s). "
                    .formatted(updateRequest.maxPlaces(), eventEntity.getOccupiedPlaces()));
        }
        LocationEntity location = locationRepository.findById(updateRequest.locationId())
                .orElseThrow(() -> new EntityNotFoundException("Location not found"));

        List<Long> subscribersList = registrationRepository.getAllUserIdByEventId(eventEntity.getId());

        Event oldEvent = eventEntityMapper.toDomain(eventEntity);

        var eventKafkaMessage = new EventKafkaMessage(
                eventEntity.getId(),
                eventEntity.getOwner().getId(),
                userEntity.getId(),
                new FieldChange<>(oldEvent.name(), updateRequest.name()),
                new FieldChange<>(oldEvent.maxPlaces(), updateRequest.maxPlaces()),
                new FieldChange<>(oldEvent.date(), updateRequest.date()),
                new FieldChange<>(oldEvent.cost(), updateRequest.cost()),
                new FieldChange<>(oldEvent.duration(), updateRequest.duration()),
                new FieldChange<>(oldEvent.locationId(), updateRequest.locationId()),
                subscribersList
        );

        Optional.ofNullable(updateRequest.name())
                .ifPresent(eventEntity::setName);
        Optional.of(updateRequest.maxPlaces())
                .ifPresent(eventEntity::setMaxPlaces);
        Optional.ofNullable(updateRequest.date())
                .ifPresent(eventEntity::setDate);
        Optional.ofNullable(updateRequest.cost())
                .ifPresent(eventEntity::setCost);
        Optional.ofNullable(updateRequest.duration())
                .ifPresent(eventEntity::setDuration);
        Optional.of(locationEntity)
                .ifPresent(eventEntity::setLocation);

        eventRepository.save(eventEntity);
        EventEntity event = getEventById(eventEntity.getId());

        eventMessageSender.sendEvent(eventKafkaMessage);

        return eventEntityEventResponseMapper.toResponse(event);
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

    public EventEntity getEventById(Long eventId) {
        return  eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event entity wasn't found id=%s"
                        .formatted(eventId)));
    }

}
