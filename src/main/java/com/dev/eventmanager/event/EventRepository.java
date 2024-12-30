package com.dev.eventmanager.event;

import com.dev.eventmanager.location.LocationEntity;
import com.dev.eventmanager.users.UserEntity;
import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import java.util.List;

public interface EventRepository extends JpaRepository<EventEntity, Long> {
    boolean existsByName(String name);

    @Transactional
    @Modifying
    @Query("""            
            UPDATE  EventEntity e
            SET  e.name = :name,
            e.maxPlaces = :maxPlaces,
            e.date = :date,
            e.cost = :cost,
            e.duration = :duration,
            e.location = :location
            WHERE e.id = :id
            """)
    void updateEvent(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("maxPlaces") Long maxPlaces,
            @Param("date") OffsetDateTime date,
            @Param("cost") BigDecimal cost,
            @Param("duration") Integer duration,
            @Param("location") LocationEntity location
    );


    @Transactional
    @Modifying
    @Query("""            
            SELECT e
            FROM EventEntity e
            WHERE (:name IS NULL OR e.name LIKE %:name%)
            AND (:durationMin IS NULL OR e.duration >= :durationMin)
            AND (:durationMax IS NULL OR e.duration <= :durationMax)
            AND (cast(:dateStartAfter as date) IS NULL OR e.date >= :dateStartAfter)
            AND (cast(:dateStartBefore as date) IS NULL OR e.date <= :dateStartBefore)
            AND (:placesMin IS NULL OR e.maxPlaces >= :placesMin)
            AND (:placesMax IS NULL OR e.maxPlaces <= :placesMax)
            AND (:locationId IS NULL OR e.location.id = :locationId)
            AND (:eventStatus IS NULL OR e.status = :eventStatus)
            AND (:costMin IS NULL OR e.cost >= :costMin)
            AND (:costMax IS NULL OR e.cost <= :costMax)
            
            """)
    List<EventEntity> filterEvents(
            @Param("name") String name,
            @Param("durationMin") Integer durationMin,
            @Param("durationMax") Integer durationMax,
            @Param("dateStartAfter") OffsetDateTime dateStartAfter,
            @Param("dateStartBefore") OffsetDateTime dateStartBefore,
            @Param("placesMin") Long placesMin,
            @Param("placesMax") Long placesMax,
            @Param("locationId") Long locationId,
            @Param("eventStatus") String eventStatus,
            @Param("costMin") BigDecimal costMin,
            @Param("costMax") BigDecimal costMax
    );

    List<EventEntity> getEventEntitiesByOwner(UserEntity userEntity);


    @Transactional
    @Modifying
    @Query("""
    SELECT e
    FROM EventEntity e
    WHERE (cast(:now as date) IS NULL OR e.date <= :now)
    AND ( e.status = :eventStatus)
    """)
    List<EventEntity> findAllRelevantEvents(@Param("now") OffsetDateTime now,
                                            @Param("eventStatus") String eventStatus);


}
