package com.liveklass.notification.adapter.outbound.persistence;

import com.liveklass.notification.application.port.outbound.NotificationRegistrationPort;
import com.liveklass.notification.domain.model.OutboxEvent;
import com.liveklass.notification.domain.model.UserNotification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public class NotificationRegistrationAdapter implements NotificationRegistrationPort {

    private final NotificationJpaRepository notificationJpaRepository;
    private final OutboxJpaRepository outboxJpaRepository;

    public NotificationRegistrationAdapter(NotificationJpaRepository notificationJpaRepository,
                                           OutboxJpaRepository outboxJpaRepository) {
        this.notificationJpaRepository = notificationJpaRepository;
        this.outboxJpaRepository = outboxJpaRepository;
    }

    @Override
    @Transactional
    public UUID save(UserNotification notification, OutboxEvent outboxEvent) {
        notificationJpaRepository.save(NotificationJpaEntity.fromDomain(notification));
        outboxJpaRepository.save(OutboxJpaEntity.fromDomain(outboxEvent));
        return notification.getId();
    }
}
