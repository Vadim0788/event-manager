package com.dev.eventmaneger.user;

import org.springframework.stereotype.Component;

@Component
public class UserEntityConverter {
    public UserEntity toEntity(User user) {
        return new UserEntity(
                user.id(),
                user.login(),
                user.age()
        );
    }

    public User toDomain(UserEntity userEntity){
        return new User(
                userEntity.getId(),
                userEntity.getLogin(),
                userEntity.getAge()
        );
    }
}
