package com.liveclass.notification.adapter.inbound.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liveclass.notification.adapter.inbound.api.dto.RegisterNotificationRequest;
import com.liveclass.notification.application.port.inbound.GetNotificationUseCase;
import com.liveclass.notification.application.port.inbound.RegisterNotificationUseCase;
import com.liveclass.notification.domain.exception.DuplicateNotificationException;
import com.liveclass.notification.domain.exception.NotificationNotFoundException;
import com.liveclass.notification.domain.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RegisterNotificationUseCase registerNotificationUseCase;

    @MockitoBean
    private GetNotificationUseCase getNotificationUseCase;

    @Test
    void 알림_접수_요청_성공() throws Exception {
        UUID id = UUID.randomUUID();
        given(registerNotificationUseCase.register(any(), any(), any(), any(), any(), any(), any())).willReturn(id);

        RegisterNotificationRequest request = new RegisterNotificationRequest(
                UUID.randomUUID(), UUID.randomUUID(), null,
                NotificationType.ENROLLMENT_COMPLETE, Channel.IN_APP,
                Map.of("courseId", "123"), null
        );

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/notifications/" + id));
    }

    @Test
    void 알림_접수_필수값_누락_시_400() throws Exception {
        String body = """
                { "userId": null, "eventId": null }
                """;

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 중복_요청_시_409() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        given(registerNotificationUseCase.register(any(), any(), any(), any(), any(), any(), any()))
                .willThrow(new DuplicateNotificationException(eventId, userId));

        RegisterNotificationRequest request = new RegisterNotificationRequest(
                userId, eventId, null,
                NotificationType.ENROLLMENT_COMPLETE, Channel.IN_APP,
                Map.of(), null
        );

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void 단건_조회_성공() throws Exception {
        UUID id = UUID.randomUUID();
        UserNotification notification = UserNotification.reconstruct(
                id, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Channel.IN_APP,
                SendStatus.PENDING, new ReferenceData(Map.of()),
                new RetryInfo(), null, null,
                null, null, null,
                LocalDateTime.now(), LocalDateTime.now()
        );
        given(getNotificationUseCase.getById(id)).willReturn(notification);

        mockMvc.perform(get("/api/notifications/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.sendStatus").value("PENDING"));
    }

    @Test
    void 단건_조회_없으면_404() throws Exception {
        UUID id = UUID.randomUUID();
        given(getNotificationUseCase.getById(id)).willThrow(new NotificationNotFoundException(id));

        mockMvc.perform(get("/api/notifications/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void 유저별_목록_조회_성공() throws Exception {
        UUID userId = UUID.randomUUID();
        given(getNotificationUseCase.getByUserId(userId, null)).willReturn(List.of());

        mockMvc.perform(get("/api/notifications/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
