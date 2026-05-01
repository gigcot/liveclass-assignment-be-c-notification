package com.liveclass.notification.application.port.inbound;

import com.liveclass.notification.domain.model.UserNotification;

import java.util.List;
import java.util.UUID;

public interface GetNotificationUseCase {

    UserNotification getById(UUID id);

    List<UserNotification> getByUserId(UUID userId, Boolean unreadOnly);
}
