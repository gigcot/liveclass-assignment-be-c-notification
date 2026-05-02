package com.liveklass.notification.adapter.outbound.persistence;

import com.liveklass.notification.domain.model.OutboxEvent;
import com.liveklass.notification.domain.model.OutboxPayload;
import com.liveklass.notification.domain.model.OutboxStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "outbox_event")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutboxJpaEntity {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "notification_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID notificationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    private OutboxStatus status;

    @Convert(converter = OutboxPayloadConverter.class)
    @Column(name = "payload", nullable = false, columnDefinition = "TEXT")
    private OutboxPayload payload;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static OutboxJpaEntity fromDomain(OutboxEvent domain) {
        OutboxJpaEntity entity = new OutboxJpaEntity();
        entity.id = domain.getId();
        entity.notificationId = domain.getNotificationId();
        entity.status = domain.getStatus();
        entity.payload = domain.getPayload();
        entity.scheduledAt = domain.getScheduledAt();
        entity.createdAt = domain.getCreatedAt();
        return entity;
    }

    public OutboxEvent toDomain() {
        return OutboxEvent.reconstruct(
                id, notificationId, status, payload, scheduledAt, createdAt
        );
    }
}
