package com.dev.eventmaneger.location;

public record LocationDto (
        Long id,
        String name,
        String address,
        int capacity,
        String description
){
}
