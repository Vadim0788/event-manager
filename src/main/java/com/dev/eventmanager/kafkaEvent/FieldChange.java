package com.dev.eventmanager.kafkaEvent;

public record FieldChange<T>(
        T oldField,
        T newField
) {}