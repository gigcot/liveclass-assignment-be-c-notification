package com.liveklass.notification.domain.exception;

import com.liveklass.notification.domain.model.SendStatus;

public class InvalidStatusTransitionException extends RuntimeException {

    private final SendStatus currentStatus;
    private final SendStatus targetStatus;

    public InvalidStatusTransitionException(SendStatus currentStatus, SendStatus targetStatus) {
        super("상태 전이 불가: " + currentStatus + " → " + targetStatus);
        this.currentStatus = currentStatus;
        this.targetStatus = targetStatus;
    }

    public SendStatus getCurrentStatus() { return currentStatus; }
    public SendStatus getTargetStatus() { return targetStatus; }
}
