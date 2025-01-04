package com.dev.eventmanager.event;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record EventUpdateRequestDto(
        String name,

        @Positive(message = "Maximum places must be greater than zero")
        Long maxPlaces,

        @Future(message = "Date must be in future")
        OffsetDateTime date,

        @PositiveOrZero(message = "Cost must be non-negative")
        BigDecimal cost,

        @Min(value = 30, message = "Duration must be greater than 30")
        Integer duration,

        Long locationId
) {

}

