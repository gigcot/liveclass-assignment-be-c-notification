package com.liveklass.notification.adapter.outbound.persistence;

import com.liveklass.notification.application.port.outbound.OutboxRepository;
import com.liveklass.notification.domain.model.OutboxEvent;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public class OutboxRepositoryAdapter implements OutboxRepository {

    private final OutboxJpaRepository jpaRepository;

    public OutboxRepositoryAdapter(OutboxJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(OutboxEvent outboxEvent) {
        jpaRepository.save(OutboxJpaEntity.fromDomain(outboxEvent));
    }

    @Override
    public List<OutboxEvent> findPendingForRelay(LocalDateTime now, int limit) {
        return jpaRepository.findPendingForRelay(now, limit)
                .stream()
                .map(OutboxJpaEntity::toDomain)
                .toList();
    }

    @Override
    public void markRelayed(UUID id) {
        jpaRepository.markRelayed(id);
    }
}
