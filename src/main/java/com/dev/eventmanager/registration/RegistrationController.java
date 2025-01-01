package com.dev.eventmanager.registration;

import com.dev.eventmanager.event.EventDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/events/registrations/")
public class RegistrationController {
    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<Void> registerUser(
            @PathVariable("eventId") Long eventId
    ) {
        log.info("Get request for user registration with an event id = {}", eventId);
        registrationService.registerUser(eventId);
        return status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/cancel/{eventId}")
    public ResponseEntity<Void> deleteRegistration(
            @PathVariable("eventId") Long eventId
    ) {
        log.info("Get request to delete a registration with an event id = {}", eventId);
        registrationService.cancelRegistration(eventId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }


    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> registerUser() {
        log.info("Get request for register for the current user.");

        return ResponseEntity.ok(registrationService.getMyRegistrations());
    }
}
