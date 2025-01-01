package com.dev.eventmanager.users;

import com.dev.eventmanager.security.jwt.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final UserEntityMapper userEntityMapper;

    public UserService(UserRepository userRepository, AuthenticationService authenticationService, UserEntityMapper userEntityMapper) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.userEntityMapper = userEntityMapper;
    }

    public User registerUser(User user) {

        if (userRepository.existsByLogin(user.login())) {
            throw new IllegalArgumentException("Username already taken");
        }

        var userToSave = userEntityMapper.toEntity(user);

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

    public UserEntity getAuthenticatedUserEntity() {
        String login = authenticationService.getCurrentUserLogin();
        return userRepository.findByLogin(login).orElseThrow(() ->
                new EntityNotFoundException(String.format("User with login (%s) not found", login))
        );
    }
}
