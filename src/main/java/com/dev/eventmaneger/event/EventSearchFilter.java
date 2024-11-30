package com.dev.eventmaneger.event;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Date;

public record EventSearchFilter(

        int durationMax,
        Date dateStartBefore,
        int placesMin,
        Long locationId,
        Status eventStatus,
        String name,
        int placesMax,
        BigDecimal costMin,
        Date dateStartAfter,
        BigDecimal costMax,
        int durationMin
) {

}
