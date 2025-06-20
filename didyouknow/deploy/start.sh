#!/bin/bash

# Spring Boot 애플리케이션 시작 스크립트

APP_NAME="didyouknow-backend"
JAR_FILE="/opt/didyouknow/didyouknow-backend.jar"
PID_FILE="/opt/didyouknow/app.pid"
LOG_FILE="/opt/didyouknow/logs/application.log"

echo "🚀 $APP_NAME 애플리케이션을 시작합니다..."

# 이미 실행 중인 프로세스가 있는지 확인
if [ -f $PID_FILE ]; then
    PID=$(cat $PID_FILE)
    if ps -p $PID > /dev/null 2>&1; then
        echo "⚠️  애플리케이션이 이미 실행 중입니다. (PID: $PID)"
        echo "🛑 기존 프로세스를 중지하려면 ./stop.sh를 실행하세요."
        exit 1
    else
        echo "🗑️  오래된 PID 파일을 삭제합니다."
        rm -f $PID_FILE
    fi
fi

# JAR 파일 존재 여부 확인
if [ ! -f $JAR_FILE ]; then
    echo "❌ JAR 파일을 찾을 수 없습니다: $JAR_FILE"
    echo "🔧 JAR 파일을 /opt/didyouknow/ 디렉토리에 업로드하세요."
    exit 1
fi

# 애플리케이션 시작
echo "▶️  애플리케이션을 백그라운드에서 시작합니다..."
nohup java -jar $JAR_FILE \
    --spring.profiles.active=prod \
    --server.port=8080 \
    --logging.file.name=$LOG_FILE > /dev/null 2>&1 &

# PID 저장
echo $! > $PID_FILE

echo "✅ 애플리케이션이 시작되었습니다!"
echo "📋 PID: $(cat $PID_FILE)"
echo "📄 로그 파일: $LOG_FILE"
echo "🌐 URL: http://YOUR_EC2_PUBLIC_IP:8080"
echo ""
echo "📊 상태 확인: ./status.sh"
echo "📝 로그 확인: tail -f $LOG_FILE" 