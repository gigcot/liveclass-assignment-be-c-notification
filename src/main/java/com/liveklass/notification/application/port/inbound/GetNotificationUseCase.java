package com.liveklass.notification.application.port.inbound;

import com.liveklass.notification.domain.model.UserNotification;

import java.util.List;
import java.util.UUID;

public interface GetNotificationUseCase {

    UserNotification getById(UUID id);

    List<UserNotification> getByUserId(UUID userId, Boolean unreadOnly);
}
