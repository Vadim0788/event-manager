package com.dev.eventmanager.web;

import java.time.OffsetDateTime;

public record ServerErrorDto(
        String message,
        String detailedMessage,
        OffsetDateTime dateTime
) {
}
