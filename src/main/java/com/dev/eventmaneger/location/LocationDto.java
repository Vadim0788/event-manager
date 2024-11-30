package com.dev.eventmaneger.location;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LocationDto (
        @Null
        Long id,
        @NotBlank
        String name,
        @NotBlank
        String address,
        @Min(5)
        @NotNull
        int capacity,
        String description
){
}
