package com.dev.eventmanager.event;

import org.springframework.stereotype.Component;

@Component
public class EventEntityEventResponseMapper {

    EventResponceDto toResponce (EventEntity eventEntity){
        return new EventResponceDto(
                eventEntity.getId(),
                eventEntity.getName(),
                eventEntity.getMaxPlaces(),
                eventEntity.getDate(),
                eventEntity.getCost(),
                eventEntity.getDuration(),
                eventEntity.getLocation().getId(),
                eventEntity.getOccupiedPlaces(),
                eventEntity.getOwner().getId(),
                eventEntity.getStatus()
        );

    }
}