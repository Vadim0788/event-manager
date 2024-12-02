package com.dev.eventmanager.event;

import java.math.BigDecimal;
import java.time.OffsetDateTime;


public record EventSearchFilter(

        Integer durationMax,
        OffsetDateTime dateStartBefore,
        Integer placesMin,
        Long locationId,
        Status eventStatus,
        String name,
        Integer placesMax,
        BigDecimal costMin,
        OffsetDateTime dateStartAfter,
        BigDecimal costMax,
        Integer durationMin
) {

}
