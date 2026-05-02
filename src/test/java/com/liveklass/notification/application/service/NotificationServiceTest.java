package com.liveklass.notification.application.service;

import com.liveklass.notification.application.port.outbound.NotificationRegistrationPort;
import com.liveklass.notification.application.port.outbound.NotificationRepository;
import com.liveklass.notification.application.port.outbound.TemplateRepository;
import com.liveklass.notification.domain.exception.DuplicateNotificationException;
import com.liveklass.notification.domain.exception.NotificationNotFoundException;
import com.liveklass.notification.domain.exception.TemplateNotFoundException;
import com.liveklass.notification.domain.model.*;
import com.liveklass.notification.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

class NotificationServiceTest {

    private NotificationService service;
    private FakeNotificationRepository notificationRepository;
    private FakeRegistrationPort registrationPort;
    private FakeTemplateRepository templateRepository;

    private static final UUID TEMPLATE_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        notificationRepository = new FakeNotificationRepository();
        registrationPort = new FakeRegistrationPort(notificationRepository);
        templateRepository = new FakeTemplateRepository();
        service = new NotificationService(notificationRepository, registrationPort, templateRepository);
    }

    @Test
    void register_templateId_있으면_해당_템플릿으로_접수() {
        UUID userId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();

        UUID id = service.register(userId, eventId, TEMPLATE_ID,
                NotificationType.ENROLLMENT_COMPLETE, Channel.IN_APP, Map.of("courseName", "Spring"), null);

        assertThat(id).isNotNull();
        assertThat(notificationRepository.findById(id)).isPresent();
        assertThat(notificationRepository.findById(id).get().getSendStatus()).isEqualTo(SendStatus.PENDING);
    }

    @Test
    void register_templateId_없으면_최신_템플릿으로_접수() {
        UUID id = service.register(UUID.randomUUID(), UUID.randomUUID(), null,
                NotificationType.ENROLLMENT_COMPLETE, Channel.IN_APP, Map.of("courseName", "Spring"), null);

        assertThat(id).isNotNull();
    }

    @Test
    void register_템플릿_없으면_예외() {
        templateRepository.empty = true;

        assertThatThrownBy(() -> service.register(UUID.randomUUID(), UUID.randomUUID(), null,
                NotificationType.ENROLLMENT_COMPLETE, Channel.IN_APP, Map.of(), null))
                .isInstanceOf(TemplateNotFoundException.class);
    }

    @Test
    void register_중복_시_예외() {
        UUID userId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();

        service.register(userId, eventId, TEMPLATE_ID,
                NotificationType.ENROLLMENT_COMPLETE, Channel.IN_APP, Map.of("courseName", "Spring"), null);

        assertThatThrownBy(() -> service.register(userId, eventId, TEMPLATE_ID,
                NotificationType.ENROLLMENT_COMPLETE, Channel.IN_APP, Map.of(), null))
                .isInstanceOf(DuplicateNotificationException.class);
    }

    @Test
    void getById_정상_조회() {
        UUID id = service.register(UUID.randomUUID(), UUID.randomUUID(), TEMPLATE_ID,
                NotificationType.ENROLLMENT_COMPLETE, Channel.IN_APP, Map.of("courseName", "Spring"), null);

        assertThat(service.getById(id).getId()).isEqualTo(id);
    }

    @Test
    void getById_없으면_예외() {
        assertThatThrownBy(() -> service.getById(UUID.randomUUID()))
                .isInstanceOf(NotificationNotFoundException.class);
    }

    @Test
    void getByUserId_읽음_안읽음_필터() {
        UUID userId = UUID.randomUUID();
        UUID id1 = service.register(userId, UUID.randomUUID(), TEMPLATE_ID,
                NotificationType.ENROLLMENT_COMPLETE, Channel.IN_APP, Map.of("courseName", "A"), null);
        service.register(userId, UUID.randomUUID(), TEMPLATE_ID,
                NotificationType.ENROLLMENT_COMPLETE, Channel.IN_APP, Map.of("courseName", "B"), null);

        notificationRepository.findById(id1).ifPresent(n -> {
            n.markRead();
            notificationRepository.save(n);
        });

        assertThat(service.getByUserId(userId, true)).hasSize(1);
        assertThat(service.getByUserId(userId, false)).hasSize(1);
        assertThat(service.getByUserId(userId, null)).hasSize(2);
    }

    @Test
    void markRead_읽음_처리() {
        UUID userId = UUID.randomUUID();
        UUID id = service.register(userId, UUID.randomUUID(), TEMPLATE_ID,
                NotificationType.ENROLLMENT_COMPLETE, Channel.IN_APP, Map.of("courseName", "A"), null);

        service.markRead(id);

        assertThat(notificationRepository.findById(id).get().getReadAt()).isNotNull();
    }

    @Test
    void markRead_이미_읽음이면_readAt_변경_없음() {
        UUID userId = UUID.randomUUID();
        UUID id = service.register(userId, UUID.randomUUID(), TEMPLATE_ID,
                NotificationType.ENROLLMENT_COMPLETE, Channel.IN_APP, Map.of("courseName", "A"), null);

        service.markRead(id);
        LocalDateTime firstReadAt = notificationRepository.findById(id).get().getReadAt();

        service.markRead(id);
        LocalDateTime secondReadAt = notificationRepository.findById(id).get().getReadAt();

        assertThat(secondReadAt).isEqualTo(firstReadAt);
    }

    @Test
    void markRead_없는_알림이면_예외() {
        assertThatThrownBy(() -> service.markRead(UUID.randomUUID()))
                .isInstanceOf(NotificationNotFoundException.class);
    }

    // ── Fakes ──────────────────────────────────────────────

    static class FakeNotificationRepository implements NotificationRepository {
        private final Map<UUID, UserNotification> store = new HashMap<>();

        @Override
        public UserNotification save(UserNotification n) { store.put(n.getId(), n); return n; }

        @Override
        public Optional<UserNotification> findById(UUID id) { return Optional.ofNullable(store.get(id)); }

        @Override
        public Optional<UserNotification> findByIdForUpdate(UUID id) { return Optional.ofNullable(store.get(id)); }

        @Override
        public List<UserNotification> findByUserId(UUID userId) {
            return store.values().stream().filter(n -> n.getUserId().equals(userId)).toList();
        }

        @Override
        public List<UserNotification> findByUserIdAndReadAtIsNull(UUID userId) {
            return store.values().stream()
                    .filter(n -> n.getUserId().equals(userId) && n.getReadAt() == null).toList();
        }

        @Override
        public List<UserNotification> findByUserIdAndReadAtIsNotNull(UUID userId) {
            return store.values().stream()
                    .filter(n -> n.getUserId().equals(userId) && n.getReadAt() != null).toList();
        }

        @Override
        public List<UserNotification> findBySendStatus(SendStatus status) {
            return store.values().stream().filter(n -> n.getSendStatus() == status).toList();
        }

        @Override
        public boolean existsByEventIdAndUserId(UUID eventId, UUID userId) {
            return store.values().stream()
                    .anyMatch(n -> n.getEventId().equals(eventId) && n.getUserId().equals(userId));
        }
    }

    static class FakeRegistrationPort implements NotificationRegistrationPort {
        private final FakeNotificationRepository repository;

        FakeRegistrationPort(FakeNotificationRepository repository) {
            this.repository = repository;
        }

        @Override
        public UUID save(UserNotification notification, OutboxEvent outboxEvent) {
            repository.save(notification);
            return notification.getId();
        }
    }

    static class FakeTemplateRepository implements TemplateRepository {
        boolean empty = false;
        private final NotificationTemplate template = NotificationTemplate.create(
                NotificationType.ENROLLMENT_COMPLETE, Channel.IN_APP,
                "{courseName} 수강 신청 완료", "{courseName} 강의에 수강 신청되었습니다."
        );

        @Override
        public Optional<NotificationTemplate> findById(UUID id) {
            return empty ? Optional.empty() : Optional.of(template);
        }

        @Override
        public Optional<NotificationTemplate> findLatestByTypeAndChannel(NotificationType type, Channel channel) {
            return empty ? Optional.empty() : Optional.of(template);
        }

        @Override
        public List<NotificationTemplate> findAll() { return List.of(); }

        @Override
        public NotificationTemplate save(NotificationTemplate t) { return t; }

        @Override
        public void deleteById(UUID id) {}
    }

}
