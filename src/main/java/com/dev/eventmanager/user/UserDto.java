package com.dev.eventmanager.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UserDto(
        Long id,
        @NotBlank
        String login,
        @Min(0)
        int age
) {
}
