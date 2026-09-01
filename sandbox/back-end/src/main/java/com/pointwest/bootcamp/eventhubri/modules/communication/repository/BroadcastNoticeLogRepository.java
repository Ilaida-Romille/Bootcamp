package com.pointwest.bootcamp.eventhubri.modules.communication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pointwest.bootcamp.eventhubri.modules.communication.entity.BroadcastNoticeLog;

public interface BroadcastNoticeLogRepository extends JpaRepository<BroadcastNoticeLog, Long> {
}
