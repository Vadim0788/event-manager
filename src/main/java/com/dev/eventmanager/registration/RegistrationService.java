package com.dev.eventmanager.registration;

import com.dev.eventmanager.event.*;
import com.dev.eventmanager.users.UserEntity;
import com.dev.eventmanager.users.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class RegistrationService {
    private final UserService userService;
    private final RegistrationRepository registrationRepository;
    private final EventEntityMapper eventEntityMapper;
    private final EventRepository eventRepository;
    private final EventDtoMapper eventDtoMapper;

    public RegistrationService(UserService userService, RegistrationRepository registrationRepository, EventEntityMapper eventEntityMapper, EventRepository eventRepository, EventDtoMapper eventDtoMapper) {
        this.userService = userService;
        this.registrationRepository = registrationRepository;
        this.eventEntityMapper = eventEntityMapper;
        this.eventRepository = eventRepository;
        this.eventDtoMapper = eventDtoMapper;
    }

    @Transactional
    public void registerUser(Long eventId) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Not found event by id=%s"
                        .formatted(eventId)));

        if (!event.getStatus().equals("WAIT_START") || event.getDate().isBefore(OffsetDateTime.now())) {
            throw new IllegalStateException("Registration for this event is not possible.");
        }
        if (event.getOccupiedPlaces() >= event.getMaxPlaces()) {
            throw new IllegalStateException("There are no available places for registration.");
        }

        UserEntity userEntity = userService.getAuthenticatedUserEntity();

        if (registrationRepository.existsByEventAndUser(event, userEntity)) {
            throw new IllegalStateException("You are already registered for this event.");
        }

        RegistrationEntity registration = new RegistrationEntity(null, event, userEntity);
        registrationRepository.save(registration);

        event.setOccupiedPlaces(event.getOccupiedPlaces() + 1);
        eventRepository.save(event);
    }

    @Transactional
    public void cancelRegistration(Long eventId) {
        EventEntity eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Not found event by id=%s"
                        .formatted(eventId)));

        if (eventEntity.getDate().isBefore(OffsetDateTime.now())) {
            throw new IllegalStateException("You cannot cancel registration for a past event.");
        }

        UserEntity userEntity = userService.getAuthenticatedUserEntity();

        RegistrationEntity registration = registrationRepository.findByEventAndUser(eventEntity, userEntity)
                .orElseThrow(() -> new EntityNotFoundException("Registration not found."));

        registrationRepository.delete(registration);

        eventEntity.setOccupiedPlaces(eventEntity.getOccupiedPlaces() - 1);
        eventRepository.save(eventEntity);
    }

    public List<EventDto> getMyRegistrations() {
        UserEntity userEntity = userService.getAuthenticatedUserEntity();
        List<EventEntity> eventEntityList = registrationRepository.findEventsByUserId(userEntity.getId());
        List<Event> eventList = eventEntityList.stream().map(eventEntityMapper::toDomain).toList();

        return eventList.stream()
                .map(eventDtoMapper::toDto)
                .toList();
    }

}
