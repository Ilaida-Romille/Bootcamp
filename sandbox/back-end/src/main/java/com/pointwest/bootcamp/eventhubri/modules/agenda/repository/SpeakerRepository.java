package com.pointwest.bootcamp.eventhubri.modules.agenda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pointwest.bootcamp.eventhubri.modules.agenda.entity.Speaker;

@Repository
public interface SpeakerRepository extends JpaRepository<Speaker, Long> {

}
