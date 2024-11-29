package com.dev.eventmaneger.user;

public class UserEntityConverter {
    public UserEntity toEntity(User user) {
        return new UserEntity(
                user.id(),
                user.login(),
                user.age(),
                user.role()
        );
    }

    public User toDomain(UserEntity userEntity){
        return new User(
                userEntity.getId(),
                userEntity.getLogin(),
                userEntity.getAge(),
                userEntity.getRole()
        );
    }
}
