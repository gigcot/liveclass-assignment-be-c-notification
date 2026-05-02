package com.liveklass.notification.application.port.inbound;

import com.liveklass.notification.domain.model.Channel;
import com.liveklass.notification.domain.model.NotificationTemplate;
import com.liveklass.notification.domain.model.NotificationType;

import java.util.List;
import java.util.UUID;

public interface TemplateUseCase {

    NotificationTemplate create(NotificationType type, Channel channel, String title, String body);

    void delete(UUID id);

    List<NotificationTemplate> findAll();
}
