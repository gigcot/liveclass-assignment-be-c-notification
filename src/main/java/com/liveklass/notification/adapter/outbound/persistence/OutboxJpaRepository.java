package com.liveklass.notification.adapter.outbound.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OutboxJpaRepository extends JpaRepository<OutboxJpaEntity, UUID> {

    @Query(value = """
            SELECT * FROM outbox_event
            WHERE status = 'PENDING'
            AND (scheduled_at IS NULL OR scheduled_at <= :now)
            ORDER BY created_at
            LIMIT :limit
            FOR UPDATE SKIP LOCKED
            """, nativeQuery = true)
    List<OutboxJpaEntity> findPendingForRelay(@Param("now") LocalDateTime now, @Param("limit") int limit);

    @Modifying
    @Query(value = "UPDATE outbox_event SET status = 'RELAYED' WHERE id = :id", nativeQuery = true)
    void markRelayed(@Param("id") UUID id);
}
