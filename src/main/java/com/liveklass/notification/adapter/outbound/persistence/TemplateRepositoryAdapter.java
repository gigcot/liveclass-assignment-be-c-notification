package com.liveklass.notification.adapter.outbound.persistence;

import com.liveklass.notification.application.port.outbound.TemplateRepository;
import com.liveklass.notification.domain.model.Channel;
import com.liveklass.notification.domain.model.NotificationTemplate;
import com.liveklass.notification.domain.model.NotificationType;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class TemplateRepositoryAdapter implements TemplateRepository {

    private final TemplateJpaRepository jpaRepository;

    public TemplateRepositoryAdapter(TemplateJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<NotificationTemplate> findById(UUID id) {
        return jpaRepository.findById(id).map(TemplateJpaEntity::toDomain);
    }

    @Override
    public Optional<NotificationTemplate> findLatestByTypeAndChannel(NotificationType type, Channel channel) {
        return jpaRepository.findTopByTypeAndChannelOrderByCreatedAtDesc(type, channel)
                .map(TemplateJpaEntity::toDomain);
    }
}
