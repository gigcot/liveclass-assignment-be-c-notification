package com.liveclass.notification.application.port.inbound;

import com.liveclass.notification.domain.model.Channel;
import com.liveclass.notification.domain.model.NotificationType;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public interface RegisterNotificationUseCase {

    UUID register(UUID userId, UUID eventId, UUID templateId,
                  NotificationType notificationType, Channel channel,
                  Map<String, String> referenceData, LocalDateTime scheduledAt);
}
