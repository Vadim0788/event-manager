package com.dev.eventmanager.event;

import com.fasterxml.jackson.annotation.JsonFormat;
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
        Long ownerId,
        @NotNull
        int maxPlaces,
        @NotNull
        int occupiedPlaces,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        @FutureOrPresent
        OffsetDateTime date,
        @Min(0)
        BigDecimal cost,
        @Min(30)
        int duration,
        @NotNull
        Long locationId,
        Status status
) {
}
