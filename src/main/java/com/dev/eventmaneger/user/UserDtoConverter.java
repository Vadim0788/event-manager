package com.dev.eventmaneger.user;

import com.dev.eventmaneger.location.Location;
import com.dev.eventmaneger.location.LocationDto;

public class UserDtoConverter {
    public UserDto toDto(User user) {
        return new UserDto(
                user.id(),
                user.login(),
                user.age(),
                user.role()
        );
    }

    public User toDomain(UserDto userDto){
        return new User(
                userDto.id(),
                userDto.login(),
                userDto.age(),
                userDto.role()
        );
    }
}
