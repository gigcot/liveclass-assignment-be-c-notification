package com.liveklass.notification.application.port.inbound;

import com.liveklass.notification.domain.model.FailureReason;

import java.util.UUID;

public interface UpdateNotificationStatusUseCase {

    void markQueued(UUID id);

    /** @return true면 claim 성공 (QUEUED→SENDING), false면 이미 처리 중 */
    boolean claim(UUID id);

    void markSent(UUID id);

    /** @return true면 재시도 가능 */
    boolean markFailed(UUID id, FailureReason reason);
}
