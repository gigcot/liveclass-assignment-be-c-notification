import logging

log = logging.getLogger(__name__)


async def send(payload: dict) -> bool:
    user_id = payload["userId"]
    message = payload["renderedMessage"]

    # TODO: 실제 이메일 발송 (SMTP / SES / SendGrid 등)
    log.info("[EMAIL MOCK] to=%s message=%s", user_id, message)
    return True
