package com.dev.eventmanager.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

import java.time.OffsetDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EventDto(
        @Null
        Long id,
        @NotBlank
        String name,
        @Min(0)
        @NotNull
        Long maxPlaces,
        @Future(message = "Дата должна быть в будущем")
        OffsetDateTime date,
        @Min(0)
        @NotNull
        BigDecimal cost,
        @Min(30)
        @NotNull
        Integer duration,
        @NotNull
        Long locationId,
        Long ownerId,
        Integer occupiedPlaces,
        String status
) {
}
