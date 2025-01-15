package com.dev.eventmanager.event;

import com.dev.eventmanager.kafkaEvent.EventKafkaMessage;
import com.dev.eventmanager.kafkaEvent.EventMessageSender;
import com.dev.eventmanager.kafkaEvent.FieldChange;
import com.dev.eventmanager.kafkaEvent.MessageType;
import com.dev.eventmanager.registration.RegistrationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventStatusUpdaterService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final EventMessageSender eventMessageSender;


    @Transactional
    @Scheduled(fixedRateString = "60000")
    public void updateEventStatuses() {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime utcNow = now.withOffsetSameInstant(ZoneOffset.UTC);
        searchAndLaunchAnEvent(utcNow);
        searchingAndFinishingEvents(utcNow);
    }

    private void searchAndLaunchAnEvent(OffsetDateTime now) {
        List<EventEntity> events = eventRepository.findAllRelevantEvents(now, EventStatus.WAIT_START.name());
        events.forEach(event -> event.setStatus(EventStatus.STARTED.name()));

        List<EventKafkaMessage> eventMessages = events.stream().map(event -> {
            List<Long> subscribersList = registrationRepository.getAllUserIdByEventId(event.getId());

            return new EventKafkaMessage(
                    event.getId(),
                    event.getOwner().getId(),
                    null,
                    new FieldChange<>(event.getName(), event.getName()),
                    new FieldChange<>(event.getMaxPlaces(), event.getMaxPlaces()),
                    new FieldChange<>(event.getDate(), event.getDate()),
                    new FieldChange<>(event.getCost(), event.getCost()),
                    new FieldChange<>(event.getDuration(), event.getDuration()),
                    new FieldChange<>(event.getLocation().getId(), event.getLocation().getId()),
                    subscribersList,
                    MessageType.STARTED
            );
        }).toList();

        eventRepository.saveAll(events);
        eventMessages.forEach(eventMessageSender::sendEvent);
    }

    private void searchingAndFinishingEvents(OffsetDateTime now) {
        List<EventEntity> events = eventRepository.findAllRelevantEvents(now, EventStatus.STARTED.name());
        events = events.stream()
                .filter(e -> e.getDate().plusMinutes(e.getDuration()).isBefore(now))
                .toList();
        events.forEach(event -> event.setStatus(EventStatus.FINISHED.name()));

        List<EventKafkaMessage> eventMessages = events.stream().map(event -> {
            List<Long> subscribersList = registrationRepository.getAllUserIdByEventId(event.getId());

            return new EventKafkaMessage(
                    event.getId(),
                    event.getOwner().getId(),
                    null,
                    new FieldChange<>(event.getName(), event.getName()),
                    new FieldChange<>(event.getMaxPlaces(), event.getMaxPlaces()),
                    new FieldChange<>(event.getDate(), event.getDate()),
                    new FieldChange<>(event.getCost(), event.getCost()),
                    new FieldChange<>(event.getDuration(), event.getDuration()),
                    new FieldChange<>(event.getLocation().getId(), event.getLocation().getId()),
                    subscribersList,
                    MessageType.FINISHED
            );
        }).toList();


        eventRepository.saveAll(events);
        eventMessages.forEach(eventMessageSender::sendEvent);
    }

}
