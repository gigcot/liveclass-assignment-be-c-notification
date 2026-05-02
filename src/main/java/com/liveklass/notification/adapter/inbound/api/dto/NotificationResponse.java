package com.liveklass.notification.adapter.inbound.api.dto;

import com.liveklass.notification.domain.model.Channel;
import com.liveklass.notification.domain.model.SendStatus;
import com.liveklass.notification.domain.model.UserNotification;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        UUID userId,
        UUID eventId,
        UUID templateId,
        Channel channel,
        SendStatus sendStatus,
        String renderedTitle,
        String renderedBody,
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
                n.getChannel(), n.getSendStatus(),
                n.getRenderedTitle(), n.getRenderedBody(),
                n.getReferenceData().toMap(),
                n.getRetryInfo().getCount(), n.getRetryInfo().getFailureReason(),
                n.getScheduledAt(), n.getSentAt(), n.getReadAt(), n.getCreatedAt()
        );
    }
}
