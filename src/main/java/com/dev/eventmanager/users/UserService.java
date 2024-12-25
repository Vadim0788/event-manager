package com.dev.eventmanager.users;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final UserEntityMapper userEntityMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserEntityMapper userEntityMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userEntityMapper = userEntityMapper;
    }

    public User registerUser(SignUppRequest singUppRequest) {

        if (userRepository.existsByLogin(singUppRequest.login())) {
            throw new IllegalArgumentException("Username already taken");
        }
        var hashedPass = passwordEncoder.encode(singUppRequest.password());

        var userToSave = new UserEntity(
                singUppRequest.login(),
                singUppRequest.age(),
                hashedPass,
                UserRole.USER.name()
        );

        var savedUserEntity = userRepository.save(userToSave);

        return userEntityMapper.toDomain(userToSave);

    }

    public UserDto findById(Long id) {
        var foundUserEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No found user by id=%s"
                                .formatted(id)
                ));

        return new UserDto(foundUserEntity.getId(), foundUserEntity.getAge(), foundUserEntity.getLogin());


    }

    public User findByLogin(String login) {
        var user = userRepository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException("User not find"));

        return userEntityMapper.toDomain(user);
    }


    public void save(UserEntity user) {
        userRepository.save(user);
    }

    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }
}
