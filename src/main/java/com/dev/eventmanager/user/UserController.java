package com.dev.eventmanager.user;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    private final UserDtoConverter dtoConverter;

    public UserController(UserService userService, UserDtoConverter dtoConverter) {
        this.userService = userService;
        this.dtoConverter = dtoConverter;
    }

    @PostMapping
    public ResponseEntity<UserDto> creatUser(
            @RequestBody @Valid UserDto userToCreate
    ) {
        log.info("Get request for create user: user {}", userToCreate);
        User createdUser = userService.createUser(
                dtoConverter.toDomain(userToCreate)
        );
        return status(HttpStatus.CREATED)
                .body(dtoConverter.toDto(createdUser));
    }

    @GetMapping("/{userId}")
    public UserDto findById(
            @PathVariable("userId") long userId
    ) {
        log.info("Get request for get user: user = {}", userId);
        var foundUser = userService.findById(userId);

        return dtoConverter.toDto(foundUser);
    }
}

