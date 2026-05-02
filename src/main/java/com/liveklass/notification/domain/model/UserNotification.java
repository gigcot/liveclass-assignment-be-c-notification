package com.liveklass.notification.domain.model;

import com.liveklass.notification.domain.exception.InvalidStatusTransitionException;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserNotification {

    private UUID id;
    private UUID userId;
    private UUID eventId;
    private UUID templateId;
    private Channel channel;
    private SendStatus sendStatus;
    private ReferenceData referenceData;
    private RetryInfo retryInfo;

    private String renderedTitle;
    private String renderedBody;

    private LocalDateTime scheduledAt;
    private LocalDateTime sentAt;
    private LocalDateTime readAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserNotification create(
            UUID userId,
            UUID eventId,
            UUID templateId,
            Channel channel,
            ReferenceData referenceData,
            String renderedTitle,
            String renderedBody,
            LocalDateTime scheduledAt
    ) {
        UserNotification notification = new UserNotification();
        notification.id = UUID.randomUUID();
        notification.userId = userId;
        notification.eventId = eventId;
        notification.templateId = templateId;
        notification.channel = channel;
        notification.sendStatus = SendStatus.PENDING;
        notification.referenceData = referenceData;
        notification.renderedTitle = renderedTitle;
        notification.renderedBody = renderedBody;
        notification.retryInfo = new RetryInfo();
        notification.scheduledAt = scheduledAt;
        notification.createdAt = LocalDateTime.now();
        notification.updatedAt = LocalDateTime.now();
        return notification;
    }

    public void markQueued() {
        if (this.sendStatus == SendStatus.QUEUED) {
            return; // 이미 QUEUED — 멱등성
        }
        if (this.sendStatus != SendStatus.PENDING) {
            throw new InvalidStatusTransitionException(this.sendStatus, SendStatus.QUEUED);
        }
        this.sendStatus = SendStatus.QUEUED;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean claim() {
        if (this.sendStatus != SendStatus.QUEUED) {
            return false;
        }
        this.sendStatus = SendStatus.SENDING;
        this.updatedAt = LocalDateTime.now();
        return true;
    }

    public void markSent() {
        if (this.sendStatus == SendStatus.SENT) {
            return; // 이미 발송 완료 — 멱등성
        }
        if (this.sendStatus != SendStatus.SENDING) {
            throw new InvalidStatusTransitionException(this.sendStatus, SendStatus.SENT);
        }
        this.sendStatus = SendStatus.SENT;
        this.sentAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void markFailed(FailureReason reason) {
        if (this.sendStatus != SendStatus.SENDING && this.sendStatus != SendStatus.QUEUED && this.sendStatus != SendStatus.PENDING) {
            throw new InvalidStatusTransitionException(this.sendStatus, SendStatus.FAILED);
        }
        this.retryInfo = retryInfo.recordFailure(reason);
        if (!retryInfo.canRetry()) {
            this.sendStatus = SendStatus.FAILED;
        }
        // 재시도 가능한 경우 상태 유지 (워커가 내부 재시도 루프에서 SENDING 유지)
        this.updatedAt = LocalDateTime.now();
    }

    public void markRead() {
        if (this.readAt != null) {
            return; // 이미 읽음 — 멱등성
        }
        this.readAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void retryManually() {
        if (this.sendStatus != SendStatus.FAILED) {
            throw new InvalidStatusTransitionException(this.sendStatus, SendStatus.PENDING);
        }
        this.retryInfo = retryInfo.reset();
        this.sendStatus = SendStatus.PENDING;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isReadyToSend() {
        return this.sendStatus == SendStatus.PENDING
                && (this.scheduledAt == null || !this.scheduledAt.isAfter(LocalDateTime.now()));
    }

    public static UserNotification reconstruct(
            UUID id, UUID userId, UUID eventId, UUID templateId, Channel channel,
            SendStatus sendStatus, ReferenceData referenceData, RetryInfo retryInfo,
            String renderedTitle, String renderedBody,
            LocalDateTime scheduledAt, LocalDateTime sentAt, LocalDateTime readAt,
            LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        UserNotification notification = new UserNotification();
        notification.id = id;
        notification.userId = userId;
        notification.eventId = eventId;
        notification.templateId = templateId;
        notification.channel = channel;
        notification.sendStatus = sendStatus;
        notification.referenceData = referenceData;
        notification.retryInfo = retryInfo;
        notification.renderedTitle = renderedTitle;
        notification.renderedBody = renderedBody;
        notification.scheduledAt = scheduledAt;
        notification.sentAt = sentAt;
        notification.readAt = readAt;
        notification.createdAt = createdAt;
        notification.updatedAt = updatedAt;
        return notification;
    }

    // Getters
    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public UUID getEventId() { return eventId; }
    public UUID getTemplateId() { return templateId; }
    public Channel getChannel() { return channel; }
    public SendStatus getSendStatus() { return sendStatus; }
    public ReferenceData getReferenceData() { return referenceData; }
    public RetryInfo getRetryInfo() { return retryInfo; }
    public String getRenderedTitle() { return renderedTitle; }
    public String getRenderedBody() { return renderedBody; }
    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public LocalDateTime getSentAt() { return sentAt; }
    public LocalDateTime getReadAt() { return readAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
