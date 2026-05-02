package com.liveclass.notification.application.port.outbound;

import com.liveclass.notification.domain.model.Channel;
import com.liveclass.notification.domain.model.NotificationTemplate;
import com.liveclass.notification.domain.model.NotificationType;

import java.util.Optional;
import java.util.UUID;

public interface TemplateRepository {

    Optional<NotificationTemplate> findById(UUID id);

    Optional<NotificationTemplate> findLatestByTypeAndChannel(NotificationType type, Channel channel);
}
