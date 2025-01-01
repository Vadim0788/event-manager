package com.dev.eventmanager.users;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserRequestMapper {
    private final PasswordEncoder passwordEncoder;

    public UserRequestMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User toDomain(SignUppRequest signUppRequest){
        var hashedPass = passwordEncoder.encode(signUppRequest.password());
        return new User(
                null,
                signUppRequest.login(),
                signUppRequest.age(),
                null,
                hashedPass
        );
    }
}
