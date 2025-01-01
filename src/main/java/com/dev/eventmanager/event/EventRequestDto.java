package com.dev.eventmanager.event;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record EventRequestDto(
        OffsetDateTime date,
        Integer duration,
        BigDecimal cost,
        Long maxPlaces,
        Long locationId,
        String name
) {

}
