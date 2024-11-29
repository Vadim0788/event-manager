package com.dev.eventmaneger.event;

import java.math.BigDecimal;
import java.util.Date;

public record Event (
        Long id ,
        String name,
        Long ownerId,
        int maxPlaces,
        int occupiedPlaces,
        Date date,
        BigDecimal cost,
        int duration,
        Long locationId,
        Status status
){

}
