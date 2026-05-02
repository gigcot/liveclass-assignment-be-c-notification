package com.liveclass.notification.adapter.outbound.persistence;

import com.liveclass.notification.domain.model.Channel;
import com.liveclass.notification.domain.model.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TemplateJpaRepository extends JpaRepository<TemplateJpaEntity, UUID> {

    Optional<TemplateJpaEntity> findTopByTypeAndChannelOrderByCreatedAtDesc(NotificationType type, Channel channel);
}
