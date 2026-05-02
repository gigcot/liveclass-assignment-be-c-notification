package com.liveklass.notification.domain.model;

import java.util.UUID;

public record OutboxPayload(
        UUID notificationId,
        UUID userId,
        Channel channel,
        String renderedMessage
) {}
