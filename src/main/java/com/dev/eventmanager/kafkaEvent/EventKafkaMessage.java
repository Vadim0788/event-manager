package com.dev.eventmanager.kafkaEvent;

import com.dev.eventmanager.event.Event;
import java.util.List;

public record EventKafkaMessage(
        Long id,
        Long userId,
        Event oldEvent,
        Event newEvent,
        Long ownerId,
        List<Long> subscribersList
) {
}
