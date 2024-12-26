package com.dev.eventmanager.users;

import com.dev.eventmanager.security.jwt.AuthenticationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")

public class UsersController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private static final Logger log = LoggerFactory.getLogger(UsersController.class);
    private final AuthenticationService authenticationService;
    private final UserDtoMapper userDtoMapper;


    public UsersController(UserService userService, PasswordEncoder passwordEncoder, AuthenticationService authenticationService, UserDtoMapper userDtoMapper) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
        this.userDtoMapper = userDtoMapper;
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(
            @RequestBody
            @Valid
            SignUppRequest singUppRequest
    ) {
        log.info("Get request for sing-up: login={}", singUppRequest.login());

        var hashedPass = passwordEncoder.encode(singUppRequest.password());
        User requestedUser = new User(
                null,
                singUppRequest.login(),
                singUppRequest.age(),
                null,
                hashedPass
        );

        var user = userService.registerUser(requestedUser);
        var userDto = userDtoMapper.toDto(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userDto);
    }

    @GetMapping
    @RequestMapping("/{id}")

    public ResponseEntity<UserDto> findById(
            @PathVariable("id") Long id
    ) {
        log.info("Get request for find user by id: id={}", id);
        var user = userService.findById(id);
        var userDto = userDtoMapper.toDto(user);
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
