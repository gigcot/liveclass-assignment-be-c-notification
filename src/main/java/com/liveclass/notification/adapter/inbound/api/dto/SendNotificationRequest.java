package com.liveclass.notification.adapter.inbound.api.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record SendNotificationRequest(
        @NotNull UUID userId,
        @NotNull UUID eventId,
        @NotNull UUID templateId,
        Map<String, String> referenceData,
        LocalDateTime scheduledAt
) {}
