package com.dev.eventmanager.security.jwt;

import com.dev.eventmanager.security.CustomUserDetails;
import com.dev.eventmanager.users.SignInRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

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
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            singInRequest.login(),
                            singInRequest.password()
                    )
            );

            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                CustomUserDetails userDetails = (CustomUserDetails) principal;
                Long userId = userDetails.getId();
                String username = userDetails.getUsername();
                List<String> roles = userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority) // Получаем строковое представление роли
                        .toList();
                // Используйте userId и username для генерации токена
                return jwtTokenManager.generateToken(username, userId, roles);
            }


//            Object principal = authentication.getPrincipal();
//            if (principal instanceof com.dev.eventmanager.users.User user) {
//                return jwtTokenManager.generateToken(user.login(), user.id(), user.role().toString());
//            }
            throw new UsernameNotFoundException("User not found user = %s"
                    .formatted(singInRequest.login())
            );

        } catch (AuthenticationException ex) {
            log.error("Authentication failed", ex);
            throw ex;
        }
    }
    public String getCurrentUserLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof com.dev.eventmanager.users.User user) {
            return user.login();
        }

        throw new IllegalStateException("Unknown principal type");
    }
}
