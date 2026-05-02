package com.liveklass.notification.domain.model;

public enum FailureReason {

    SEND_FAILED,
    TIMEOUT,
    CHANNEL_UNAVAILABLE;

    public String toMessage() {
        return name().toLowerCase().replace('_', ' ');
    }
}
