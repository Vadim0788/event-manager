package com.dev.eventmanager.user;

public record User(
        Long id,
        String login,
        int age
) {
}
