package com.dev.eventmanager.event;

import java.math.BigDecimal;

import java.time.OffsetDateTime;


public record Event(
        Long id,
        String name,
        Long maxPlaces,
        OffsetDateTime date,
        BigDecimal cost,
        Integer duration,
        Long locationId
) {
}
