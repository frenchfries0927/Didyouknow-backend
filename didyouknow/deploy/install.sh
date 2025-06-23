#!/bin/bash

# EC2 Ubuntu 인스턴스 초기 설정 스크립트

echo "🚀 EC2 인스턴스 초기 설정을 시작합니다..."

# 시스템 업데이트
sudo apt-get update -y
sudo apt-get upgrade -y

# Java 17 설치
echo "☕ Java 17 설치 중..."
sudo apt-get install -y openjdk-17-jdk

# Java 버전 확인
java -version

# 필수 패키지 설치
sudo apt-get install -y wget curl unzip htop

# 애플리케이션 디렉토리 생성
sudo mkdir -p /opt/didyouknow
sudo mkdir -p /opt/didyouknow/logs
sudo mkdir -p /opt/didyouknow/uploads

# 사용자 권한 설정
sudo chown -R $USER:$USER /opt/didyouknow

echo "✅ 초기 설정이 완료되었습니다!"
echo "🔧 다음 단계: JAR 파일을 /opt/didyouknow/ 디렉토리에 업로드하세요." 