package com.liveclass.notification.application.port.inbound;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public interface SendNotificationUseCase {

    UUID request(UUID userId, UUID eventId, UUID templateId,
                 Map<String, String> referenceData, LocalDateTime scheduledAt);
}
