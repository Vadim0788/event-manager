package com.dev.eventmaneger.user;

import org.springframework.stereotype.Component;

@Component
public class UserDtoConverter {
    public UserDto toDto(User user) {
        return new UserDto(
                user.id(),
                user.login(),
                user.age()
        );
    }

    public User toDomain(UserDto userDto){
        return new User(
                userDto.id(),
                userDto.login(),
                userDto.age()
        );
    }
}
