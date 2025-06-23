#!/bin/bash

# 자동 배포 스크립트 (로컬에서 실행)

# 설정
EC2_HOST="13.125.111.127"  # EC2 퍼블릭 IP
EC2_USER="ubuntu"  # EC2 사용자명
KEY_FILE="/Users/sangyeoplee/Downloads/spring-key.pem"  # .pem 키 파일 경로
JAR_FILE="build/libs/didyouknow-backend.jar"

echo "🚀 Didyouknow 백엔드 자동 배포 시작"
echo "================================"

# 입력값 검증
if [ -z "$EC2_HOST" ]; then
    echo "❌ EC2_HOST를 설정하세요."
    echo "스크립트 상단의 EC2_HOST 변수에 EC2 퍼블릭 IP를 입력하세요."
    exit 1
fi

if [ -z "$KEY_FILE" ]; then
    echo "❌ KEY_FILE을 설정하세요."
    echo "스크립트 상단의 KEY_FILE 변수에 .pem 키 파일 경로를 입력하세요."
    exit 1
fi

if [ ! -f "$KEY_FILE" ]; then
    echo "❌ 키 파일을 찾을 수 없습니다: $KEY_FILE"
    exit 1
fi

if [ ! -f "$JAR_FILE" ]; then
    echo "❌ JAR 파일을 찾을 수 없습니다: $JAR_FILE"
    echo "gradle bootJar 명령으로 빌드를 먼저 실행하세요."
    exit 1
fi

# SSH 연결 테스트
echo "🔍 EC2 연결 테스트..."
ssh -i "$KEY_FILE" -o ConnectTimeout=5 -o StrictHostKeyChecking=no "$EC2_USER@$EC2_HOST" "echo '연결 성공'" || {
    echo "❌ EC2 연결 실패. 다음을 확인하세요:"
    echo "   - EC2 인스턴스가 실행 중인지"
    echo "   - 보안 그룹에 SSH(22) 포트가 열려있는지"
    echo "   - 키 파일 경로와 권한이 올바른지"
    exit 1
}

# 기존 애플리케이션 중지
echo "🛑 기존 애플리케이션 중지..."
ssh -i "$KEY_FILE" "$EC2_USER@$EC2_HOST" "cd /opt/didyouknow && ./stop.sh" 2>/dev/null || true

# JAR 파일 업로드
echo "📤 JAR 파일 업로드 중..."
scp -i "$KEY_FILE" "$JAR_FILE" "$EC2_USER@$EC2_HOST:/opt/didyouknow/"

# 배포 스크립트 업로드
echo "📤 배포 스크립트 업로드 중..."
scp -i "$KEY_FILE" deploy/*.sh "$EC2_USER@$EC2_HOST:/opt/didyouknow/"

# 스크립트 실행 권한 부여 및 애플리케이션 시작
echo "🚀 애플리케이션 시작..."
ssh -i "$KEY_FILE" "$EC2_USER@$EC2_HOST" "
    cd /opt/didyouknow
    chmod +x *.sh
    ./start.sh
"

echo "✅ 배포 완료!"
echo "🌐 애플리케이션 URL: http://$EC2_HOST:8080"
echo "📊 상태 확인: ssh -i $KEY_FILE $EC2_USER@$EC2_HOST 'cd /opt/didyouknow && ./status.sh'" 