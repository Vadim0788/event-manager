package com.dev.eventmanager.event;

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

    @Transactional
    @Scheduled(fixedRateString = "60000")
    public void updateEventStatuses() {
        OffsetDateTime now  = OffsetDateTime.now();
        OffsetDateTime utcNow = now.withOffsetSameInstant(ZoneOffset.UTC);
        searchAndLaunchAnEvent(utcNow);
        searchingAndFinishingEvents(utcNow);
    }

    private void searchAndLaunchAnEvent(OffsetDateTime now) {
        List<EventEntity> events = eventRepository.findAllRelevantEvents(now, EventStatus.WAIT_START.name());
        events.forEach(event -> event.setStatus(EventStatus.STARTED.name()));
        eventRepository.saveAll(events);
    }
    private void searchingAndFinishingEvents(OffsetDateTime now) {
        List<EventEntity> events = eventRepository.findAllRelevantEvents(now, EventStatus.STARTED.name());
        events = events.stream()
                .filter(e -> e.getDate().plusMinutes(e.getDuration()).isBefore(now))
                .toList();
        events.forEach(event -> event.setStatus(EventStatus.FINISHED.name()));
        eventRepository.saveAll(events);
    }

}
