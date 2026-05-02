package com.liveclass.notification.adapter.inbound.api.dto;

import com.liveclass.notification.domain.model.FailureReason;

public record FailedCallbackRequest(FailureReason reason) {}
