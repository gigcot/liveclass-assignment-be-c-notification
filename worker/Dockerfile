FROM python:3.12-slim
ENV PYTHONIOENCODING=utf-8
ENV LANG=C.UTF-8
WORKDIR /app
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
COPY main.py .
COPY senders/ senders/
# make generate-client 실행 후 생성되는 클라이언트 코드
COPY notification_client/ notification_client/
CMD ["python", "main.py"]
