package com.liveklass.notification.application.port.inbound;

import java.util.UUID;

public interface MarkReadUseCase {

    void markRead(UUID id);
}
