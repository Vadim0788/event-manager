package com.dev.eventmaneger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LocationController {

    @GetMapping
    public String hello() {
        return "hello";
    }
}
