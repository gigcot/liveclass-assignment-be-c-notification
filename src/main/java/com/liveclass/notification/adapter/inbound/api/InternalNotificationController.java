package com.liveclass.notification.adapter.inbound.api;

import com.liveclass.notification.adapter.inbound.api.dto.FailedCallbackRequest;
import com.liveclass.notification.application.port.inbound.UpdateNotificationStatusUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/internal/notifications")
public class InternalNotificationController {

    private final UpdateNotificationStatusUseCase updateStatusUseCase;

    public InternalNotificationController(UpdateNotificationStatusUseCase updateStatusUseCase) {
        this.updateStatusUseCase = updateStatusUseCase;
    }

    @PostMapping("/{id}/queued")
    public ResponseEntity<Void> markQueued(@PathVariable UUID id) {
        updateStatusUseCase.markQueued(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/claim")
    public ResponseEntity<Void> claim(@PathVariable UUID id) {
        boolean claimed = updateStatusUseCase.claim(id);
        return claimed ? ResponseEntity.ok().build() : ResponseEntity.status(409).build();
    }

    @PostMapping("/{id}/sent")
    public ResponseEntity<Void> markSent(@PathVariable UUID id) {
        updateStatusUseCase.markSent(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/failed")
    public ResponseEntity<Map<String, Boolean>> markFailed(
            @PathVariable UUID id,
            @RequestBody FailedCallbackRequest request) {
        boolean retry = updateStatusUseCase.markFailed(id, request.reason());
        return ResponseEntity.ok(Map.of("retry", retry));
    }
}
