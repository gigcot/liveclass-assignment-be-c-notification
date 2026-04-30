package com.liveclass.notification.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class NotificationTemplateTest {

    @Test
    @DisplayName("생성 시 type, channel 설정")
    void shouldCreateWithTypeAndChannel() {
        NotificationTemplate template = NotificationTemplate.create(
                NotificationType.CLASS_REMINDER,
                Channel.IN_APP,
                "강의 알림",
                "{courseName} 강의가 내일 시작됩니다"
        );

        assertThat(template.getId()).isNotNull();
        assertThat(template.getType()).isEqualTo(NotificationType.CLASS_REMINDER);
        assertThat(template.getChannel()).isEqualTo(Channel.IN_APP);
    }

    @Test
    @DisplayName("플레이스홀더 치환 - body")
    void shouldRenderBody() {
        NotificationTemplate template = NotificationTemplate.create(
                NotificationType.CLASS_REMINDER,
                Channel.EMAIL,
                "알림",
                "{userName}님, {courseName} 강의가 내일 시작됩니다"
        );
        ReferenceData data = new ReferenceData(Map.of(
                "userName", "홍길동",
                "courseName", "Spring Boot 실전"
        ));

        String result = template.renderBody(data);

        assertThat(result).isEqualTo("홍길동님, Spring Boot 실전 강의가 내일 시작됩니다");
    }

    @Test
    @DisplayName("플레이스홀더 치환 - title")
    void shouldRenderTitle() {
        NotificationTemplate template = NotificationTemplate.create(
                NotificationType.PAYMENT_CONFIRMED,
                Channel.IN_APP,
                "{courseName} 결제 완료",
                "내용"
        );
        ReferenceData data = new ReferenceData(Map.of("courseName", "JPA 입문"));

        String result = template.renderTitle(data);

        assertThat(result).isEqualTo("JPA 입문 결제 완료");
    }

    @Test
    @DisplayName("템플릿 문구 수정")
    void shouldUpdateTitleAndBody() {
        NotificationTemplate template = NotificationTemplate.create(
                NotificationType.CLASS_REMINDER,
                Channel.IN_APP,
                "기존 제목",
                "기존 내용"
        );

        template.update("새 제목", "새 내용");

        assertThat(template.getTitle()).isEqualTo("새 제목");
        assertThat(template.getBody()).isEqualTo("새 내용");
    }
}
