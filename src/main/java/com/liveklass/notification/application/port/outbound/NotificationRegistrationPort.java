package com.liveklass.notification.application.port.outbound;

import com.liveklass.notification.domain.model.OutboxEvent;
import com.liveklass.notification.domain.model.UserNotification;

import java.util.UUID;

public interface NotificationRegistrationPort {

    UUID save(UserNotification notification, OutboxEvent outboxEvent);
}
