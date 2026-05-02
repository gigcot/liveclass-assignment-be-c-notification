package com.liveklass.notification.adapter.inbound.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liveklass.notification.adapter.inbound.api.dto.CreateTemplateRequest;
import com.liveklass.notification.application.port.inbound.TemplateUseCase;
import com.liveklass.notification.domain.exception.TemplateNotFoundException;
import com.liveklass.notification.domain.model.Channel;
import com.liveklass.notification.domain.model.NotificationTemplate;
import com.liveklass.notification.domain.model.NotificationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TemplateController.class)
class TemplateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TemplateUseCase templateUseCase;

    @Test
    void 템플릿_생성_성공() throws Exception {
        UUID id = UUID.randomUUID();
        NotificationTemplate template = NotificationTemplate.reconstruct(
                id, NotificationType.ENROLLMENT_COMPLETE, Channel.IN_APP,
                "제목", "본문", LocalDateTime.now(), LocalDateTime.now()
        );
        given(templateUseCase.create(any(), any(), any(), any())).willReturn(template);

        CreateTemplateRequest request = new CreateTemplateRequest(
                NotificationType.ENROLLMENT_COMPLETE, Channel.IN_APP, "제목", "본문");

        mockMvc.perform(post("/api/templates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void 템플릿_생성_필수값_누락_시_400() throws Exception {
        String body = "{}";

        mockMvc.perform(post("/api/templates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 템플릿_삭제_성공() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/templates/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void 없는_템플릿_삭제_시_404() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(TemplateNotFoundException.byId(id)).when(templateUseCase).delete(id);

        mockMvc.perform(delete("/api/templates/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void 전체_목록_조회() throws Exception {
        given(templateUseCase.findAll()).willReturn(List.of());

        mockMvc.perform(get("/api/templates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
