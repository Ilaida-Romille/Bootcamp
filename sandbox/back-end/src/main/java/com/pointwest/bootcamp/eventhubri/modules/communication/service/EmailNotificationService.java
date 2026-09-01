package com.pointwest.bootcamp.eventhubri.modules.communication.service;

import com.pointwest.bootcamp.eventhubri.modules.communication.dto.EmailSendRequestDto;
import com.pointwest.bootcamp.eventhubri.modules.communication.dto.NotificationLogResponseDto;

public interface EmailNotificationService {

    NotificationLogResponseDto sendBroadcastEmail(EmailSendRequestDto request, String authenticatedUserEmail);
}
