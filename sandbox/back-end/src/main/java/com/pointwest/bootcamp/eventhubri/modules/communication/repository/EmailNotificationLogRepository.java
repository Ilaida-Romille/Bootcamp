package com.pointwest.bootcamp.eventhubri.modules.communication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pointwest.bootcamp.eventhubri.modules.communication.entity.EmailNotificationLog;

public interface EmailNotificationLogRepository extends JpaRepository<EmailNotificationLog, Long> {
}
