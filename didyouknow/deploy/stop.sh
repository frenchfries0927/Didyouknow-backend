#!/bin/bash

# Spring Boot 애플리케이션 중지 스크립트

APP_NAME="didyouknow-backend"
PID_FILE="/opt/didyouknow/app.pid"

echo "🛑 $APP_NAME 애플리케이션을 중지합니다..."

# PID 파일 존재 여부 확인
if [ ! -f $PID_FILE ]; then
    echo "❌ PID 파일을 찾을 수 없습니다. 애플리케이션이 실행 중이지 않을 수 있습니다."
    exit 1
fi

# PID 읽기
PID=$(cat $PID_FILE)

# 프로세스 존재 여부 확인
if ! ps -p $PID > /dev/null 2>&1; then
    echo "❌ PID $PID에 해당하는 프로세스를 찾을 수 없습니다."
    echo "🗑️  PID 파일을 삭제합니다."
    rm -f $PID_FILE
    exit 1
fi

# 프로세스 종료
echo "⏹️  프로세스 종료 중... (PID: $PID)"
kill $PID

# 종료 확인 (최대 10초 대기)
for i in {1..10}; do
    if ! ps -p $PID > /dev/null 2>&1; then
        echo "✅ 애플리케이션이 정상적으로 종료되었습니다."
        rm -f $PID_FILE
        exit 0
    fi
    echo "⏳ 종료 대기 중... ($i/10)"
    sleep 1
done

# 강제 종료
echo "⚠️  정상 종료되지 않아 강제 종료합니다."
kill -9 $PID
rm -f $PID_FILE
echo "✅ 애플리케이션이 강제 종료되었습니다." 