package com.dev.eventmaneger.user;

import com.dev.eventmaneger.location.Location;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final  UserRepository userRepository;

    private final UserEntityConverter entityConverter;

    public UserService(UserRepository userRepository, UserEntityConverter entityConverter) {
        this.userRepository = userRepository;
        this.entityConverter = entityConverter;
    }

    public User createUser(User user) {

        if(userRepository.existsByLogin(user.login())) {
            throw new IllegalArgumentException("User login already taken");
        }
        var entityToSave = entityConverter.toEntity(user);
        return entityConverter.toDomain(
                userRepository.save(entityToSave));
    }

    public User findById(long userId) {
        if(!userRepository.existsById(userId)) {
            throw  new EntityNotFoundException("Not found user by id=%s"
                    .formatted(userId));
        }

        var entity = userRepository.getReferenceById(userId);
        return entityConverter.toDomain(entity);
    }
}
