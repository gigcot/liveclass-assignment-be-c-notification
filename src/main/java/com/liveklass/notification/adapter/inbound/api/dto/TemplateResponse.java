package com.liveklass.notification.adapter.inbound.api.dto;

import com.liveklass.notification.domain.model.Channel;
import com.liveklass.notification.domain.model.NotificationTemplate;
import com.liveklass.notification.domain.model.NotificationType;

import java.time.LocalDateTime;
import java.util.UUID;

public record TemplateResponse(
        UUID id,
        NotificationType type,
        Channel channel,
        String title,
        String body,
        LocalDateTime createdAt
) {
    public static TemplateResponse from(NotificationTemplate t) {
        return new TemplateResponse(t.getId(), t.getType(), t.getChannel(), t.getTitle(), t.getBody(), t.getCreatedAt());
    }
}
