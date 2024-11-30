package com.dev.eventmaneger.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EventDto(
        @Null
        Long id,
        @NotBlank
        String name,
        @OneToMany
        @JoinColumn(name = "")
        Long ownerId,
        @NotNull
        int maxPlaces,
        @NotNull
        int occupiedPlaces,
        @FutureOrPresent
        Date date,
        @Min(0)
        BigDecimal cost,
        @Min(30)
        int duration,
        @NotNull
        Long locationId,
        Status status
) {
}
