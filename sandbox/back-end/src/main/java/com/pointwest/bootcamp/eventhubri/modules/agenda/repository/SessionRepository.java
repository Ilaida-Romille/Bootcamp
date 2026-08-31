package com.pointwest.bootcamp.eventhubri.modules.agenda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pointwest.bootcamp.eventhubri.modules.agenda.entity.Session;

import jakarta.transaction.Transactional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByTrackIdOrderByStartTimeAsc(Long trackId);

    void deleteByTrackId(Long trackId);

    void deleteByTrackAgendaId(Long agendaId);

    void deleteByTrackAgendaEventId(Long eventId);
}
