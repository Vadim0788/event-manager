package com.dev.eventmanager.event;

import java.math.BigDecimal;
import java.time.OffsetDateTime;


public record EventFilterRequest(
        Integer durationMin,
        Integer durationMax,
        OffsetDateTime dateStartAfter,
        OffsetDateTime dateStartBefore,
        Long placesMin,
        Long placesMax,
        Long locationId,
        String eventStatus,
        String name,
        BigDecimal costMin,
        BigDecimal costMax
) {

}