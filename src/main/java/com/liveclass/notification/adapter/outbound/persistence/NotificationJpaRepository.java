package com.liveclass.notification.adapter.outbound.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationJpaRepository extends JpaRepository<NotificationJpaEntity, UUID> {

    List<NotificationJpaEntity> findByUserId(UUID userId);

    List<NotificationJpaEntity> findByUserIdAndReadAtIsNull(UUID userId);

    List<NotificationJpaEntity> findByUserIdAndReadAtIsNotNull(UUID userId);

    boolean existsByEventIdAndUserId(UUID eventId, UUID userId);
}
