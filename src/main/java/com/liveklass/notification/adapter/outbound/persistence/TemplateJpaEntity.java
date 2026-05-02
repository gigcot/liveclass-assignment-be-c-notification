package com.liveklass.notification.adapter.outbound.persistence;

import com.liveklass.notification.domain.model.Channel;
import com.liveklass.notification.domain.model.NotificationTemplate;
import com.liveklass.notification.domain.model.NotificationType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notification_template")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TemplateJpaEntity {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false, length = 10)
    private Channel channel;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "body", nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static TemplateJpaEntity fromDomain(NotificationTemplate domain) {
        TemplateJpaEntity entity = new TemplateJpaEntity();
        entity.id = domain.getId();
        entity.type = domain.getType();
        entity.channel = domain.getChannel();
        entity.title = domain.getTitle();
        entity.body = domain.getBody();
        entity.createdAt = domain.getCreatedAt();
        entity.updatedAt = domain.getUpdatedAt();
        return entity;
    }

    public NotificationTemplate toDomain() {
        return NotificationTemplate.reconstruct(
                id, type, channel, title, body, createdAt, updatedAt
        );
    }
}
