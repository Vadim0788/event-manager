package com.dev.eventmanager.users;

import jakarta.validation.constraints.*;

public record SignUppRequest(
        @NotBlank
        @Size(min = 4)
        String login,
        @NotNull
        @Min(0)
        @Max(100)
        int age,
        @NotBlank
        @Size(min = 4)
        String password
) {
}
