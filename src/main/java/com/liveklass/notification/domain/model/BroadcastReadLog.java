package com.liveklass.notification.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class BroadcastReadLog {

    private UUID id;
    private UUID notificationId;
    private UUID userId;
    private LocalDateTime readAt;

    public static BroadcastReadLog create(UUID notificationId, UUID userId) {
        BroadcastReadLog log = new BroadcastReadLog();
        log.id = UUID.randomUUID();
        log.notificationId = notificationId;
        log.userId = userId;
        log.readAt = LocalDateTime.now();
        return log;
    }

    // Getters
    public UUID getId() { return id; }
    public UUID getNotificationId() { return notificationId; }
    public UUID getUserId() { return userId; }
    public LocalDateTime getReadAt() { return readAt; }
}
