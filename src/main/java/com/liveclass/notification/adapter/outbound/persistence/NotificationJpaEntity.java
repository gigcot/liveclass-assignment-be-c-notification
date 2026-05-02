package com.liveclass.notification.adapter.outbound.persistence;

import com.liveclass.notification.domain.model.Channel;
import com.liveclass.notification.domain.model.ReferenceData;
import com.liveclass.notification.domain.model.RetryInfo;
import com.liveclass.notification.domain.model.SendStatus;
import com.liveclass.notification.domain.model.UserNotification;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "user_notification",
        uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "user_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationJpaEntity {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID userId;

    @Column(name = "event_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID eventId;

    @Column(name = "template_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID templateId;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false, length = 10)
    private Channel channel;

    @Enumerated(EnumType.STRING)
    @Column(name = "send_status", nullable = false, length = 20)
    private SendStatus sendStatus;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "notification_reference_data",
            joinColumns = @JoinColumn(name = "notification_id"))
    @MapKeyColumn(name = "data_key")
    @Column(name = "data_value")
    private Map<String, String> referenceData;

    @Column(name = "rendered_title")
    private String renderedTitle;

    @Column(name = "rendered_body", columnDefinition = "TEXT")
    private String renderedBody;

    @Column(name = "retry_count", nullable = false)
    private int retryCount;

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static NotificationJpaEntity fromDomain(UserNotification domain) {
        NotificationJpaEntity entity = new NotificationJpaEntity();
        entity.id = domain.getId();
        entity.userId = domain.getUserId();
        entity.eventId = domain.getEventId();
        entity.templateId = domain.getTemplateId();
        entity.channel = domain.getChannel();
        entity.sendStatus = domain.getSendStatus();
        entity.referenceData = domain.getReferenceData().toMap();
        entity.renderedTitle = domain.getRenderedTitle();
        entity.renderedBody = domain.getRenderedBody();
        entity.retryCount = domain.getRetryInfo().getCount();
        entity.failureReason = domain.getRetryInfo().getFailureReason();
        entity.scheduledAt = domain.getScheduledAt();
        entity.sentAt = domain.getSentAt();
        entity.readAt = domain.getReadAt();
        entity.createdAt = domain.getCreatedAt();
        entity.updatedAt = domain.getUpdatedAt();
        return entity;
    }

    public UserNotification toDomain() {
        return UserNotification.reconstruct(
                id, userId, eventId, templateId, channel, sendStatus,
                new ReferenceData(referenceData),
                new RetryInfo(retryCount, failureReason),
                renderedTitle, renderedBody,
                scheduledAt, sentAt, readAt, createdAt, updatedAt
        );
    }
}
