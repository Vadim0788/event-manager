package com.dev.eventmanager.event;

import java.math.BigDecimal;

import java.time.OffsetDateTime;

public record Event(
        Long id,
        String name,
        Long ownerId,
        int maxPlaces,
        int occupiedPlaces,
        OffsetDateTime date,
        BigDecimal cost,
        int duration,
        Long locationId,
        Status status
) {

}
