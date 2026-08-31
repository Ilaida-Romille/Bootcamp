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

        // Organization Specific
        @Query("""
                            SELECT e FROM Event e
                            WHERE e.status = 'PUBLISHED'
                              AND (e.isPrivate = false OR e.organization.id = :userOrgId)
                              AND (:keyword IS NULL OR LOWER(e.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
                              AND (:eventType IS NULL OR e.eventType = :eventType)
                              AND (:location IS NULL OR LOWER(e.locationAddress) LIKE LOWER(CONCAT('%', :location, '%')))
                              AND (cast(:startFrom as date) IS NULL OR e.startTime >= :startFrom)
                              AND (cast(:startTo as date) IS NULL OR e.startTime <= :startTo)
                        """)
        Page<Event> findPublishedEvents(
                        @Param("userOrgId") Long userOrgId,
                        @Param("keyword") String keyword,
                        @Param("eventType") Event.EventType eventType,
                        @Param("location") String location,
                        @Param("startFrom") LocalDateTime startFrom,
                        @Param("startTo") LocalDateTime startTo,
                        Pageable pageable);

        @Query("""
                        SELECT e FROM Event e
                        WHERE e.organization.id = :organizationId
                        AND (:status IS NULL OR e.status = :status)
                        AND (:eventType IS NULL OR e.eventType = :eventType)
                        AND (:keyword IS NULL OR (
                            LOWER(e.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                            LOWER(e.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
                        ))
                        """)
        Page<Event> searchOrganizationEvents(
                        @Param("organizationId") Long organizationId,
                        @Param("status") Event.Status status,
                        @Param("eventType") Event.EventType eventType,
                        @Param("keyword") String keyword,
                        Pageable pageable);

        // Attendee Specific
        @Query("""
                        SELECT e FROM Event e
                        WHERE e.status = com.pointwest.bootcamp.eventhubri.modules.event.entity.Event.Status.PUBLISHED
                        AND (e.isPrivate = false OR e.organization.id = :userOrgId)
                        AND (:keyword IS NULL OR LOWER(e.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
                        AND (:eventType IS NULL OR e.eventType = :eventType)
                        AND (:startFrom IS NULL OR e.startTime >= :startFrom)
                        AND (:startTo IS NULL OR e.startTime <= :startTo)
                        AND (:location IS NULL OR LOWER(e.locationAddress) LIKE LOWER(CONCAT('%', :location, '%')))
                        """)
        Page<Event> searchDiscoverable(
                        @Param("userOrgId") Long userOrgId,
                        @Param("keyword") String keyword,
                        @Param("eventType") Event.EventType eventType,
                        @Param("startFrom") LocalDateTime startFrom,
                        @Param("startTo") LocalDateTime startTo,
                        @Param("location") String location,
                        Pageable pageable);

        Optional<Event> findByIdAndStatusAndIsPrivateFalse(Long id, Event.Status status);

}