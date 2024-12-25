package com.dev.eventmanager.users;

import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {
    public User toDomain(UserEntity userEntity){
        return new User(
                userEntity.getId(),
                userEntity.getLogin(),
                userEntity.getAge(),
                UserRole.valueOf(userEntity.getRole())
        );
    }

    public UserEntity toEntity(User user, String passwordHash){
        return new UserEntity(
                user.login(),
                user.age(),
                passwordHash,
                user.role().name()
        );
    }
}
