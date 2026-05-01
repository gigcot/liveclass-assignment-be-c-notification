package com.liveclass.notification.application.service;

import com.liveclass.notification.domain.exception.DuplicateNotificationException;
import com.liveclass.notification.domain.exception.NotificationNotFoundException;
import com.liveclass.notification.domain.model.ReferenceData;
import com.liveclass.notification.domain.model.UserNotification;
import com.liveclass.notification.application.port.inbound.GetNotificationUseCase;
import com.liveclass.notification.application.port.inbound.SendNotificationUseCase;
import com.liveclass.notification.application.port.outbound.NotificationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class NotificationService implements SendNotificationUseCase, GetNotificationUseCase {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public UUID request(UUID userId, UUID eventId, UUID templateId,
                        Map<String, String> referenceData, LocalDateTime scheduledAt) {

        if (notificationRepository.existsByEventIdAndUserId(eventId, userId)) {
            throw new DuplicateNotificationException(eventId, userId);
        }

        UserNotification notification = UserNotification.create(
                userId, eventId, templateId,
                new ReferenceData(referenceData),
                scheduledAt
        );

        notificationRepository.save(notification);
        return notification.getId();
    }

    @Override
    public UserNotification getById(UUID id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(id));
    }

    @Override
    public List<UserNotification> getByUserId(UUID userId, Boolean unreadOnly) {
        if (unreadOnly == null) {
            return notificationRepository.findByUserId(userId);
        }
        if (unreadOnly) {
            return notificationRepository.findByUserIdAndReadAtIsNull(userId);
        }
        return notificationRepository.findByUserIdAndReadAtIsNotNull(userId);
    }
}
