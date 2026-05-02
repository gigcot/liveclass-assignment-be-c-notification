package com.liveklass.notification.application.port.inbound;

import com.liveklass.notification.domain.model.Channel;
import com.liveklass.notification.domain.model.NotificationType;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public interface RegisterNotificationUseCase {

    UUID register(UUID userId, UUID eventId, UUID templateId,
                  NotificationType notificationType, Channel channel,
                  Map<String, String> referenceData, LocalDateTime scheduledAt);
}
