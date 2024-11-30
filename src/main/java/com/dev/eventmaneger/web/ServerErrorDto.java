package com.dev.eventmaneger.web;

import java.time.LocalDateTime;

public record ServerErrorDto (
        String message,
        String detailedMessage,
        LocalDateTime dateTime
){
}
