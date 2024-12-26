package com.dev.eventmanager.users;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final UserEntityMapper userEntityMapper;

    public UserService(UserRepository userRepository, UserEntityMapper userEntityMapper) {
        this.userRepository = userRepository;
        this.userEntityMapper = userEntityMapper;
    }

    public User registerUser(User user) {

        if (userRepository.existsByLogin(user.login())) {
            throw new IllegalArgumentException("Username already taken");
        }

        var userToSave = new UserEntity(
                user.login(),
                user.age(),
                user.passwordHash(),
                UserRole.USER.name()
        );

        var savedUserEntity = userRepository.save(userToSave);

        return userEntityMapper.toDomain(savedUserEntity);

    }

    public User findById(Long id) {
        var foundUserEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No found user by id=%s"
                                .formatted(id)
                ));

        return  userEntityMapper.toDomain(foundUserEntity);
    }

    public User findByLogin(String login) {
        var user = userRepository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException("User not find"));

        return userEntityMapper.toDomain(user);
    }


    public void save(UserEntity user) {
        userRepository.save(user);
    }

    public boolean doesNotExistByLogin(String login) {
        return ! userRepository.existsByLogin(login);
    }
}
