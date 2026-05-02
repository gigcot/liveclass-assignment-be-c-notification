package com.liveklass.notification.domain.exception;

import java.util.UUID;

public class DuplicateNotificationException extends RuntimeException {

    private final UUID eventId;
    private final UUID userId;

    public DuplicateNotificationException(UUID eventId, UUID userId) {
        super("이미 해당 이벤트에 대한 알림이 존재합니다. eventId=" + eventId + ", userId=" + userId);
        this.eventId = eventId;
        this.userId = userId;
    }

    public UUID getEventId() { return eventId; }
    public UUID getUserId() { return userId; }
}
