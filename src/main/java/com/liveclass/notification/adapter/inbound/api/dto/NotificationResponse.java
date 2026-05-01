package com.liveclass.notification.adapter.inbound.api.dto;

import com.liveclass.notification.domain.model.SendStatus;
import com.liveclass.notification.domain.model.UserNotification;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        UUID userId,
        UUID eventId,
        UUID templateId,
        SendStatus sendStatus,
        Map<String, String> referenceData,
        int retryCount,
        String failureReason,
        LocalDateTime scheduledAt,
        LocalDateTime sentAt,
        LocalDateTime readAt,
        LocalDateTime createdAt
) {
    public static NotificationResponse from(UserNotification n) {
        return new NotificationResponse(
                n.getId(), n.getUserId(), n.getEventId(), n.getTemplateId(),
                n.getSendStatus(), n.getReferenceData().toMap(),
                n.getRetryInfo().getCount(), n.getRetryInfo().getFailureReason(),
                n.getScheduledAt(), n.getSentAt(), n.getReadAt(), n.getCreatedAt()
        );
    }
}
