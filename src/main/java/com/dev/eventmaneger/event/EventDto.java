package com.dev.eventmaneger.event;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.math.BigDecimal;
import java.util.Date;

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
