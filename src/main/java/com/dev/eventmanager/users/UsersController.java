package com.dev.eventmanager.users;

import com.dev.eventmanager.security.jwt.AuthenticationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearerAuth")
public class UsersController {

    private final UserService userService;

    private static final Logger log = LoggerFactory.getLogger(UsersController.class);
    private final AuthenticationService authenticationService;


    public UsersController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(
            @RequestBody
            @Valid
            SignUppRequest singUppRequest
    ) {
        log.info("Get request for sing-up: login={}", singUppRequest.login());
        var user = userService.registerUser(singUppRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserDto(user.id(), user.age(), user.login()));
    }

    @GetMapping
    @RequestMapping("/{id}")

    public ResponseEntity<UserDto> findById(
            @PathVariable("id") Long id
    ) {
        log.info("Get request for find user by id: id={}", id);
        var userDto = userService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userDto);
    }

    @PostMapping
    @RequestMapping("/auth")
    public ResponseEntity<JwtTokenResponse> authenticate(
            @RequestBody
            @Valid
            SignInRequest signInRequest
    ) {
        log.info("Get request for sing-in: login={}", signInRequest.login());
        var token = authenticationService.authenticateUser(signInRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new JwtTokenResponse(token));
    }
}
