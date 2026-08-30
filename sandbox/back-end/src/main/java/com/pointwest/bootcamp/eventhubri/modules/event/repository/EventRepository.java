package com.pointwest.bootcamp.eventhubri.modules.event.repository;

import com.pointwest.bootcamp.eventhubri.modules.event.entity.Event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByOrganizationId(Long organizationId);

    Optional<Event> findByIdAndOrganizationId(Long id, Long organizationId);

    @Query("""
            SELECT e FROM Event e
            WHERE e.status = com.pointwest.bootcamp.eventhubri.modules.event.entity.Event.Status.PUBLISHED
            AND e.isPrivate = false
            AND (:keyword IS NULL OR LOWER(e.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
            AND (:eventType IS NULL OR e.eventType = :eventType)
            AND (:startFrom IS NULL OR e.startTime >= :startFrom)
            AND (:startTo IS NULL OR e.startTime <= :startTo)
            AND (:location IS NULL OR LOWER(e.locationAddress) LIKE LOWER(CONCAT('%', :location, '%')))
            """)
    Page<Event> searchDiscoverable(
            @Param("keyword") String keyword,
            @Param("eventType") Event.EventType eventType,
            @Param("startFrom") LocalDateTime startFrom,
            @Param("startTo") LocalDateTime startTo,
            @Param("location") String location,
            Pageable pageable);

    Optional<Event> findByIdAndStatusAndIsPrivateFalse(Long id, Event.Status status);

}