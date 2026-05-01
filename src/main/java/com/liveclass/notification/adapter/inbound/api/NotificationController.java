package com.liveclass.notification.adapter.inbound.api;

import com.liveclass.notification.adapter.inbound.api.dto.NotificationResponse;
import com.liveclass.notification.adapter.inbound.api.dto.SendNotificationRequest;
import com.liveclass.notification.domain.model.UserNotification;
import com.liveclass.notification.application.port.inbound.GetNotificationUseCase;
import com.liveclass.notification.application.port.inbound.SendNotificationUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final SendNotificationUseCase sendNotificationUseCase;
    private final GetNotificationUseCase getNotificationUseCase;

    public NotificationController(SendNotificationUseCase sendNotificationUseCase,
                                  GetNotificationUseCase getNotificationUseCase) {
        this.sendNotificationUseCase = sendNotificationUseCase;
        this.getNotificationUseCase = getNotificationUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> send(@Valid @RequestBody SendNotificationRequest request) {
        UUID id = sendNotificationUseCase.request(
                request.userId(), request.eventId(), request.templateId(),
                request.referenceData(), request.scheduledAt()
        );
        return ResponseEntity.created(URI.create("/api/notifications/" + id)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getById(@PathVariable UUID id) {
        UserNotification notification = getNotificationUseCase.getById(id);
        return ResponseEntity.ok(NotificationResponse.from(notification));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<NotificationResponse>> getByUserId(
            @PathVariable UUID userId,
            @RequestParam(required = false) Boolean unreadOnly) {
        List<NotificationResponse> responses = getNotificationUseCase.getByUserId(userId, unreadOnly)
                .stream()
                .map(NotificationResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }
}
