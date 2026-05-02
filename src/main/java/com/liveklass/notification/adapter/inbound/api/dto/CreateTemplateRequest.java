package com.liveklass.notification.adapter.inbound.api.dto;

import com.liveklass.notification.domain.model.Channel;
import com.liveklass.notification.domain.model.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTemplateRequest(
        @NotNull NotificationType type,
        @NotNull Channel channel,
        @NotBlank String title,
        @NotBlank String body
) {}
