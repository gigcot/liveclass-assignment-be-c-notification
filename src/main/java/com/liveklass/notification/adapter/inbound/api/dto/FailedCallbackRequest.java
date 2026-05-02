package com.liveklass.notification.adapter.inbound.api.dto;

import com.liveklass.notification.domain.model.FailureReason;

public record FailedCallbackRequest(FailureReason reason) {}
