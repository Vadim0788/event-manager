package com.dev.eventmaneger.user;

public record UserDto(
        Long id,
        String login,
        int age,
        String role
) {
}
