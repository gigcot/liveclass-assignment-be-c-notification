package com.liveclass.notification.application.port.outbound;

import com.liveclass.notification.domain.model.OutboxEvent;
import com.liveclass.notification.domain.model.UserNotification;

import java.util.UUID;

public interface NotificationRegistrationPort {

    UUID save(UserNotification notification, OutboxEvent outboxEvent);
}
