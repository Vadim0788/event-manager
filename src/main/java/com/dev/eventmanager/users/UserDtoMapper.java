package com.dev.eventmanager.users;

import org.springframework.stereotype.Component;

@Component
public class UserDtoMapper {
    public User toEntity(UserDto userDto, UserRole role, String passwordHash) {
        return new User(
                userDto.id(),
                userDto.login(),
                userDto.age(),
                role,
                passwordHash
        );
    }

    public UserDto toDto (User user) {
        return new UserDto(
                user.id(),
                user.age(),
                user.login()
        );
    }
}
