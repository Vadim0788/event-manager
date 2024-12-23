package com.dev.eventmanager.location;

public record Location(
        Long id,
        String name,
        String address,
        int capacity,
        String description
) {
}
