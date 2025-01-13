package com.dev.eventmanager.kafkaEvent;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record EventKafkaMessage(
        Long eventId,
        Long ownerId,
        Long changedById,
        FieldChange<String> name,
        FieldChange<Long> maxPlaces,
        FieldChange<OffsetDateTime> date,
        FieldChange<BigDecimal> cost,
        FieldChange<Integer> duration,
        FieldChange<Long> locationId,
        List<Long> subscribersId
) {
}
