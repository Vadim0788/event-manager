package com.dev.eventmaneger.event;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Null;

import java.math.BigDecimal;
import java.util.Date;

public record EventDto(
        @Null
        Long id,
        String name,
        Long ownerId,
        int maxPlaces,
        int occupiedPlaces,
        Date date,
        @Min(0)
        BigDecimal cost,
        @Min(30)
        int duration,
        Long locationId,
        Status status
) {
}
