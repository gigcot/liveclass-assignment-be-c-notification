package com.liveclass.notification.application.service;

import com.liveclass.notification.application.port.outbound.NotificationRepository;
import com.liveclass.notification.domain.exception.DuplicateNotificationException;
import com.liveclass.notification.domain.exception.NotificationNotFoundException;
import com.liveclass.notification.domain.model.ReferenceData;
import com.liveclass.notification.domain.model.SendStatus;
import com.liveclass.notification.domain.model.UserNotification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

class NotificationServiceTest {

    private NotificationService service;
    private FakeNotificationRepository repository;

    @BeforeEach
    void setUp() {
        repository = new FakeNotificationRepository();
        service = new NotificationService(repository);
    }

    @Test
    void request_정상_생성() {
        UUID userId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID templateId = UUID.randomUUID();

        UUID id = service.request(userId, eventId, templateId, Map.of("key", "value"), null);

        assertThat(id).isNotNull();
        assertThat(repository.findById(id)).isPresent();
        assertThat(repository.findById(id).get().getSendStatus()).isEqualTo(SendStatus.PENDING);
    }

    @Test
    void request_중복_시_예외() {
        UUID userId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID templateId = UUID.randomUUID();

        service.request(userId, eventId, templateId, Map.of(), null);

        assertThatThrownBy(() -> service.request(userId, eventId, templateId, Map.of(), null))
                .isInstanceOf(DuplicateNotificationException.class);
    }

    @Test
    void getById_정상_조회() {
        UUID id = service.request(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Map.of(), null);

        UserNotification result = service.getById(id);

        assertThat(result.getId()).isEqualTo(id);
    }

    @Test
    void getById_없으면_예외() {
        assertThatThrownBy(() -> service.getById(UUID.randomUUID()))
                .isInstanceOf(NotificationNotFoundException.class);
    }

    @Test
    void getByUserId_전체_조회() {
        UUID userId = UUID.randomUUID();
        service.request(userId, UUID.randomUUID(), UUID.randomUUID(), Map.of(), null);
        service.request(userId, UUID.randomUUID(), UUID.randomUUID(), Map.of(), null);

        List<UserNotification> result = service.getByUserId(userId, null);

        assertThat(result).hasSize(2);
    }

    @Test
    void getByUserId_안읽음만_조회() {
        UUID userId = UUID.randomUUID();
        UUID id1 = service.request(userId, UUID.randomUUID(), UUID.randomUUID(), Map.of(), null);
        service.request(userId, UUID.randomUUID(), UUID.randomUUID(), Map.of(), null);

        // id1을 읽음 처리
        UserNotification n = repository.findById(id1).get();
        n.markRead();
        repository.save(n);

        List<UserNotification> unread = service.getByUserId(userId, true);
        List<UserNotification> read = service.getByUserId(userId, false);

        assertThat(unread).hasSize(1);
        assertThat(read).hasSize(1);
    }

    /**
     * DB 없이 테스트하기 위한 인메모리 가짜 저장소
     */
    static class FakeNotificationRepository implements NotificationRepository {

        private final Map<UUID, UserNotification> store = new HashMap<>();

        @Override
        public UserNotification save(UserNotification notification) {
            store.put(notification.getId(), notification);
            return notification;
        }

        @Override
        public Optional<UserNotification> findById(UUID id) {
            return Optional.ofNullable(store.get(id));
        }

        @Override
        public List<UserNotification> findByUserId(UUID userId) {
            return store.values().stream()
                    .filter(n -> n.getUserId().equals(userId))
                    .toList();
        }

        @Override
        public List<UserNotification> findByUserIdAndReadAtIsNull(UUID userId) {
            return store.values().stream()
                    .filter(n -> n.getUserId().equals(userId) && n.getReadAt() == null)
                    .toList();
        }

        @Override
        public List<UserNotification> findByUserIdAndReadAtIsNotNull(UUID userId) {
            return store.values().stream()
                    .filter(n -> n.getUserId().equals(userId) && n.getReadAt() != null)
                    .toList();
        }

        @Override
        public boolean existsByEventIdAndUserId(UUID eventId, UUID userId) {
            return store.values().stream()
                    .anyMatch(n -> n.getEventId().equals(eventId) && n.getUserId().equals(userId));
        }
    }
}
