package com.liveklass.notification.application.service;

import com.liveklass.notification.application.port.outbound.TemplateRepository;
import com.liveklass.notification.domain.exception.TemplateNotFoundException;
import com.liveklass.notification.domain.model.Channel;
import com.liveklass.notification.domain.model.NotificationTemplate;
import com.liveklass.notification.domain.model.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

class TemplateServiceTest {

    private TemplateService service;
    private FakeTemplateRepository templateRepository;

    @BeforeEach
    void setUp() {
        templateRepository = new FakeTemplateRepository();
        service = new TemplateService(templateRepository);
    }

    @Test
    void 템플릿_생성() {
        NotificationTemplate template = service.create(
                NotificationType.ENROLLMENT_COMPLETE, Channel.IN_APP, "{courseName} 등록", "본문");

        assertThat(template.getId()).isNotNull();
        assertThat(templateRepository.findById(template.getId())).isPresent();
    }

    @Test
    void 템플릿_삭제() {
        NotificationTemplate template = service.create(
                NotificationType.ENROLLMENT_COMPLETE, Channel.IN_APP, "제목", "본문");

        service.delete(template.getId());

        assertThat(templateRepository.findById(template.getId())).isEmpty();
    }

    @Test
    void 없는_템플릿_삭제_시_예외() {
        assertThatThrownBy(() -> service.delete(UUID.randomUUID()))
                .isInstanceOf(TemplateNotFoundException.class);
    }

    @Test
    void 전체_목록_조회() {
        service.create(NotificationType.ENROLLMENT_COMPLETE, Channel.IN_APP, "제목1", "본문1");
        service.create(NotificationType.ENROLLMENT_COMPLETE, Channel.EMAIL, "제목2", "본문2");

        assertThat(service.findAll()).hasSize(2);
    }

    // ── Fake ──────────────────────────────────────────────

    static class FakeTemplateRepository implements TemplateRepository {
        private final Map<UUID, NotificationTemplate> store = new HashMap<>();

        @Override
        public Optional<NotificationTemplate> findById(UUID id) {
            return Optional.ofNullable(store.get(id));
        }

        @Override
        public Optional<NotificationTemplate> findLatestByTypeAndChannel(NotificationType type, Channel channel) {
            return store.values().stream()
                    .filter(t -> t.getType() == type && t.getChannel() == channel)
                    .max(Comparator.comparing(NotificationTemplate::getCreatedAt));
        }

        @Override
        public List<NotificationTemplate> findAll() {
            return new ArrayList<>(store.values());
        }

        @Override
        public NotificationTemplate save(NotificationTemplate template) {
            store.put(template.getId(), template);
            return template;
        }

        @Override
        public void deleteById(UUID id) {
            store.remove(id);
        }
    }
}
