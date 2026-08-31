package com.pointwest.bootcamp.eventhubri.modules.agenda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pointwest.bootcamp.eventhubri.modules.agenda.entity.Track;

import jakarta.transaction.Transactional;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {
    List<Track> findByAgendaIdOrderByDisplayOrderAsc(Long agendaId);

    void deleteByAgendaId(Long agendaId);

    void deleteByAgendaEventId(Long eventId);
}
