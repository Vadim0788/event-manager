package com.dev.eventmanager.security.jwt;

import com.dev.eventmanager.users.SignInRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    private final AuthenticationManager authenticationManager;

    private final JwtTokenManager jwtTokenManager;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtTokenManager jwtTokenManager) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenManager = jwtTokenManager;
    }

    public String authenticateUser(@Valid SignInRequest singInRequest) {
        try {
            log.error("Authentication started");
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            singInRequest.login(),
                            singInRequest.password()
                    )
            );
            return jwtTokenManager.generateToken(singInRequest.login());
        } catch (AuthenticationException ex) {
            log.error("Authentication failed", ex);
            throw ex;
        }
    }
}
