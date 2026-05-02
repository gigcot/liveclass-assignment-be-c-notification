package com.liveclass.notification.adapter.inbound.api.dto;

import com.liveclass.notification.domain.model.Channel;
import com.liveclass.notification.domain.model.NotificationType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record RegisterNotificationRequest(
        @NotNull UUID userId,
        @NotNull UUID eventId,
        UUID templateId,
        @NotNull NotificationType notificationType,
        @NotNull Channel channel,
        Map<String, String> referenceData,
        LocalDateTime scheduledAt
) {}
