package com.liveklass.notification.application.port.outbound;

import com.liveklass.notification.domain.model.OutboxEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OutboxRepository {

    void save(OutboxEvent outboxEvent);

    List<OutboxEvent> findPendingForRelay(LocalDateTime now, int limit);

    void markRelayed(UUID id);
}
