# ADR-004: 중복 발송 방지

## 컨텍스트

중복 발송이 발생할 수 있는 시나리오는 세 가지다.

1. 같은 이벤트로 API 접수가 두 번 들어오는 경우
2. Relay가 lpush 성공 후 commit 실패로 인해 같은 outbox_event를 재처리하는 경우
3. Worker가 동일한 notificationId를 중복으로 처리하는 경우

## 결정

**시나리오 1 — `(userId, eventId, channel)` unique constraint**

같은 이벤트에 대해 동일 수신자 + 동일 채널로 알림이 두 번 접수되는 것을 DB 제약으로 막는다.
접수 시점에 SELECT로 확인하지 않고, INSERT 실패(constraint violation)로 처리한다.
SELECT 후 INSERT 방식은 동시 요청 시 둘 다 통과하는 race condition이 생긴다.

**시나리오 2 — Relay SKIP LOCKED**

Relay는 `SELECT FOR UPDATE SKIP LOCKED`로 outbox_event를 폴링한다.
여러 Relay 인스턴스가 동시에 실행되더라도 같은 row를 중복으로 가져가지 않는다.
lpush → commit 순서로 처리하여, commit 실패 시 outbox_event가 PENDING으로 롤백되어 다음 폴링에서 재처리된다.

**시나리오 3 — SENDING 상태 + SELECT FOR UPDATE**

Worker는 발송 전에 `/internal/notifications/{id}/claim`을 호출하여 `QUEUED → SENDING` 상태 전환을 원자적으로 수행한다.
`claim()`은 `SELECT FOR UPDATE`로 row를 잠근 후 상태를 확인하고, QUEUED인 경우에만 SENDING으로 전환하고 커밋한다.
이미 SENDING/SENT/FAILED 상태라면 409를 반환하고, Worker는 해당 알림 처리를 skip한다.

SENDING 상태는 Worker가 점유 중임을 나타내며, 내부 재시도 루프(최대 3회, exponential backoff 4s → 8s → 16s) 동안 유지된다.
발송 성공 시 SENT, 최종 실패 시 FAILED로 전환된다.

`markSent()`는 멱등하게 구현되어 이미 SENT인 경우 예외 없이 리턴한다.

## 결과

- API 접수 중복: DB unique constraint로 차단
- Relay 중복: SKIP LOCKED으로 차단
- Worker 중복 처리: SENDING 상태 + SELECT FOR UPDATE로 차단
- 발송 채널 호출이 DB 트랜잭션 밖에서 일어나지만, SENDING 상태가 application-level 락 역할을 하여 중복 발송을 방지한다
- row 레벨 락이므로 다른 알림 처리에는 영향을 주지 않는다
