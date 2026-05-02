package com.liveklass.notification.application.port.outbound;

import com.liveklass.notification.domain.model.Channel;
import com.liveklass.notification.domain.model.NotificationTemplate;
import com.liveklass.notification.domain.model.NotificationType;

import java.util.Optional;
import java.util.UUID;

public interface TemplateRepository {

    Optional<NotificationTemplate> findById(UUID id);

    Optional<NotificationTemplate> findLatestByTypeAndChannel(NotificationType type, Channel channel);
}
