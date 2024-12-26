package com.dev.eventmanager.init;

import com.dev.eventmanager.users.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserInitializer {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public DefaultUserInitializer(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeDefaultUsers() {
        if (userService.doesNotExistByLogin("admin")) {
            UserEntity admin = new UserEntity(
                    "admin",
                    22,
                    passwordEncoder.encode("admin"),
                    UserRole.ADMIN.name()
            );
            userService.save(admin);
            System.out.println("Default admin created: login=admin, password=admin");
        }

        if (userService.doesNotExistByLogin("user")) {
            UserEntity user = new UserEntity(
                    "user",
                    22,
                    passwordEncoder.encode("user"),
                    UserRole.USER.name()
            );
            userService.save(user);
            System.out.println("Default user created: login=user, password=user");
        }
    }
}