package com.dev.eventmanager.event;


import com.dev.eventmanager.users.UserEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record EventResponseDto (
        Long id,
        String name,
        Long maxPlaces,
        OffsetDateTime date,
        BigDecimal cost,
        Integer duration,
        Long locationId,
        Integer occupiedPlaces,
        Long owner,
        String status
){
}
