package com.liveclass.notification.application.port.outbound;

import com.liveclass.notification.domain.model.OutboxEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OutboxRepository {

    void save(OutboxEvent outboxEvent);

    List<OutboxEvent> findPendingForRelay(LocalDateTime now, int limit);

    void markRelayed(UUID id);
}
