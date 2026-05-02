FROM python:3.12-slim
WORKDIR /app
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
COPY main.py .
# make generate-client 실행 후 생성되는 클라이언트 코드
COPY notification_client/ notification_client/
CMD ["python", "main.py"]
