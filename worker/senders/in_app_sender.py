import logging

log = logging.getLogger(__name__)


async def send(payload: dict) -> bool:
    user_id = payload["userId"]
    message = payload["renderedMessage"]

    # TODO: 실제 인앱 발송 (WebSocket / FCM / SSE 등)
    log.info("[IN_APP MOCK] to=%s message=%s", user_id, message)
    return True
