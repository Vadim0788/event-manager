package com.dev.eventmanager.registration;

import com.dev.eventmanager.event.EventEntity;
import com.dev.eventmanager.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Long> {
    Optional<RegistrationEntity> findByEventAndUser(EventEntity eventEntity, UserEntity userEntity);

    @Query("""
                SELECT r.event
                FROM RegistrationEntity r
                JOIN FETCH r.event.location
                JOIN FETCH r.event.owner
                WHERE r.user.id = :userId
            """)
    List<EventEntity> findEventsByUserId(@Param("userId") Long userId);

    boolean existsByEventAndUser(EventEntity event, UserEntity userEntity);

    @Query("SELECT r.user.id FROM RegistrationEntity r WHERE r.event.id = :eventId")
    List<Long> getAllUserIdByEventId(@Param("eventId") Long eventId);
}
