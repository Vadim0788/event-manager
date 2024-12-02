package com.dev.eventmanager.event;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

import java.time.OffsetDateTime;

import java.util.Collection;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

    boolean existsByName(String name);

    @Transactional
    @Modifying
    @Query("""            
            UPDATE  EventEntity e
            SET  e.name = :name,
            e.maxPlaces = :max_places,
            e.date = :date,
            e.cost = :cost,
            e.duration = :duration,
            e.locationId= :loc_id
            WHERE e.id = :id
            """)
    void updateEvent(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("max_places") int maxPlaces,
            @Param("date") OffsetDateTime date,
            @Param("cost") BigDecimal cost,
            @Param("duration") int duration,
            @Param("loc_id") Long locationId);


    @Query("""
            SELECT e FROM EventEntity e
            WHERE (:durationMax IS NULL OR e.duration <= :durationMax)
            AND (:durationMin IS NULL OR e.duration >= :durationMin)
            AND (cast(:dateStartBefore as date)  IS NULL OR e.date < :dateStartBefore)
            AND (cast(:dateStartAfter as date)  IS NULL OR e.date > :dateStartAfter)
            AND (:placesMin IS NULL OR e.occupiedPlaces >= :placesMin)
            AND (:placesMax IS NULL OR e.occupiedPlaces <= :placesMax)
            AND (:locationId IS NULL OR e.locationId = :locationId)
            AND (:eventStatus IS NULL OR e.status = :eventStatus)
            AND (:name IS NULL OR LOWER(e.name) LIKE CONCAT('%', LOWER(:name), '%'))
            AND (:costMin IS NULL OR e.cost >= :costMin)
            AND (:costMax IS NULL OR e.cost <= :costMax)
            """)
    Collection<EventEntity> searchEvents(
            @Param("durationMax") Integer durationMax,
            @Param("durationMin") Integer durationMin,

            @Param("dateStartBefore") OffsetDateTime dateStartBefore,
            @Param("dateStartAfter") OffsetDateTime dateStartAfter,
            @Param("placesMin") Integer placesMin,
            @Param("placesMax") Integer placesMax,
            @Param("locationId") Long locationId,
            @Param("eventStatus") Status eventStatus,
            @Param("name") String name,
            @Param("costMin") BigDecimal costMin,
            @Param("costMax") BigDecimal costMax
    );

    @Query("""
                SELECT e FROM EventEntity e
                WHERE e.ownerId = :userId
            """)
    List<EventEntity> findByOwnerId(@Param("userId") Long userId);

    @Query("""
            SELECT COUNT(e) > 0
            FROM EventEntity e
            WHERE e.locationId = :locationId
            """)
    boolean existsByLocationId(@Param("locationId") Long locationId);

    @Transactional
    @Modifying
    @Query("DELETE FROM EventEntity e WHERE e.id = :eventId")
    void deleteEventById(@Param("eventId") Long eventId);
}
