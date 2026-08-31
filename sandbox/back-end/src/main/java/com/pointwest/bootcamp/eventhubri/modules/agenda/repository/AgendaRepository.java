package com.pointwest.bootcamp.eventhubri.modules.agenda.repository;

import com.pointwest.bootcamp.eventhubri.modules.agenda.entity.Agenda;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    List<Agenda> findByEventIdOrderByAgendaDateAsc(Long eventId);

    void deleteByEventId(Long eventId);
}