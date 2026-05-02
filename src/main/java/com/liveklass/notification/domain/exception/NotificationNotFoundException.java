package com.liveklass.notification.domain.exception;

import java.util.UUID;

public class NotificationNotFoundException extends RuntimeException {

    private final UUID notificationId;

    public NotificationNotFoundException(UUID notificationId) {
        super("알림을 찾을 수 없습니다: " + notificationId);
        this.notificationId = notificationId;
    }

    public UUID getNotificationId() { return notificationId; }
}
