package com.liveklass.notification.adapter.inbound.api.dto;

import com.liveklass.notification.domain.model.Channel;
import com.liveklass.notification.domain.model.NotificationType;
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
