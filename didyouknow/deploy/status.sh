#!/bin/bash

# Spring Boot 애플리케이션 상태 확인 스크립트

APP_NAME="didyouknow-backend"
PID_FILE="/opt/didyouknow/app.pid"
LOG_FILE="/opt/didyouknow/logs/application.log"

echo "📊 $APP_NAME 상태 확인"
echo "================================"

# PID 파일 확인
if [ ! -f $PID_FILE ]; then
    echo "❌ 상태: 중지됨 (PID 파일 없음)"
    exit 1
fi

# PID 읽기
PID=$(cat $PID_FILE)

# 프로세스 상태 확인
if ps -p $PID > /dev/null 2>&1; then
    echo "✅ 상태: 실행 중"
    echo "📋 PID: $PID"
    
    # 메모리 사용량 확인
    MEMORY=$(ps -p $PID -o rss= | awk '{print $1/1024 " MB"}')
    echo "💾 메모리 사용량: $MEMORY"
    
    # CPU 사용량 확인
    CPU=$(ps -p $PID -o %cpu= | awk '{print $1 "%"}')
    echo "⚡ CPU 사용량: $CPU"
    
    # 실행 시간 확인
    UPTIME=$(ps -p $PID -o etime= | awk '{print $1}')
    echo "⏱️  실행 시간: $UPTIME"
    
    # 포트 확인
    echo "🌐 포트 상태:"
    netstat -tlnp 2>/dev/null | grep ":8080 " || echo "   포트 8080이 열려있지 않습니다."
    
    # 애플리케이션 응답 확인
    echo "🔍 헬스체크:"
    HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/actuator/health 2>/dev/null || echo "000")
    if [ "$HTTP_STATUS" = "200" ]; then
        echo "   ✅ 애플리케이션이 정상 응답 중"
    else
        echo "   ⚠️  애플리케이션 응답 없음 (HTTP: $HTTP_STATUS)"
    fi
    
else
    echo "❌ 상태: 중지됨 (프로세스 없음)"
    echo "🗑️  오래된 PID 파일을 삭제합니다."
    rm -f $PID_FILE
fi

echo ""
echo "📄 최근 로그 (마지막 10줄):"
echo "--------------------------------"
if [ -f $LOG_FILE ]; then
    tail -10 $LOG_FILE
else
    echo "로그 파일을 찾을 수 없습니다: $LOG_FILE"
fi 