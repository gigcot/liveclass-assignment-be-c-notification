package com.liveklass.notification.adapter.outbound.persistence;

import com.liveklass.notification.domain.model.Channel;
import com.liveklass.notification.domain.model.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TemplateJpaRepository extends JpaRepository<TemplateJpaEntity, UUID> {

    Optional<TemplateJpaEntity> findTopByTypeAndChannelOrderByCreatedAtDesc(NotificationType type, Channel channel);
}
