# 🚀 Didyouknow 백엔드 EC2 배포 가이드

## 📋 사전 준비사항

1. **AWS 계정** 및 **EC2 권한**
2. **SSH 키 페어** (.pem 파일)
3. **로컬에 빌드된 JAR 파일** (`didyouknow-backend.jar`)

---

## 🖥️ 1단계: EC2 인스턴스 생성

### AWS Console에서 EC2 생성:

1. **EC2 대시보드** → **인스턴스 시작**
2. **AMI 선택**: `Ubuntu Server 22.04 LTS` (프리티어 사용 가능)
3. **인스턴스 타입**: `t2.micro` (프리티어) 또는 `t3.small` (권장)
4. **키 페어**: 기존 키 선택 또는 새로 생성
5. **보안 그룹 설정**:
   ```
   SSH (22) - 내 IP
   HTTP (80) - 0.0.0.0/0
   HTTPS (443) - 0.0.0.0/0
   Custom TCP (8080) - 0.0.0.0/0  # Spring Boot 포트
   ```

---

## 🔧 2단계: EC2 초기 설정

### SSH 접속:
```bash
ssh -i your-key.pem ubuntu@YOUR_EC2_PUBLIC_IP
```

### 초기 설정 스크립트 실행:
```bash
# 파일 다운로드
wget https://raw.githubusercontent.com/your-repo/install.sh
chmod +x install.sh
./install.sh
```

또는 수동 설정:
```bash
# 시스템 업데이트
sudo apt update && sudo apt upgrade -y

# Java 17 설치
sudo apt install -y openjdk-17-jdk

# 애플리케이션 디렉토리 생성
sudo mkdir -p /opt/didyouknow/{logs,uploads}
sudo chown -R ubuntu:ubuntu /opt/didyouknow
```

---

## 📦 3단계: 애플리케이션 배포

### 방법 1: 자동 배포 스크립트 사용 (권장)

1. **로컬에서 배포 스크립트 설정**:
   ```bash
   cd Didyouknow-backend/didyouknow
   vi deploy/deploy.sh
   ```

2. **설정값 입력**:
   ```bash
   EC2_HOST="YOUR_EC2_PUBLIC_IP"  # EC2 퍼블릭 IP
   KEY_FILE="path/to/your-key.pem"  # 키 파일 경로
   ```

3. **배포 실행**:
   ```bash
   ./deploy/deploy.sh
   ```

### 방법 2: 수동 배포

1. **JAR 파일 업로드**:
   ```bash
   scp -i your-key.pem build/libs/didyouknow-backend.jar ubuntu@YOUR_EC2_IP:/opt/didyouknow/
   ```

2. **배포 스크립트 업로드**:
   ```bash
   scp -i your-key.pem deploy/*.sh ubuntu@YOUR_EC2_IP:/opt/didyouknow/
   ```

3. **SSH 접속하여 애플리케이션 시작**:
   ```bash
   ssh -i your-key.pem ubuntu@YOUR_EC2_IP
   cd /opt/didyouknow
   chmod +x *.sh
   ./start.sh
   ```

---

## 🔍 4단계: 배포 확인

### 애플리케이션 상태 확인:
```bash
cd /opt/didyouknow
./status.sh
```

### 브라우저에서 확인:
- **헬스체크**: `http://YOUR_EC2_IP:8080/actuator/health`
- **API 문서**: `http://YOUR_EC2_IP:8080/swagger-ui/index.html`
- **피드 API**: `http://YOUR_EC2_IP:8080/api/feeds`

---

## 🛠️ 5단계: 프론트엔드 연결

### app.config.js 수정:
```javascript
extra: {
  apiUrl: "http://YOUR_EC2_PUBLIC_IP:8080",  // EC2 IP로 변경
}
```

### 프론트엔드 재시작:
```bash
cd Didyouknow-frontend
npm start
```

---

## 📋 관리 명령어

### 애플리케이션 관리:
```bash
cd /opt/didyouknow

# 상태 확인
./status.sh

# 애플리케이션 중지
./stop.sh

# 애플리케이션 시작
./start.sh

# 로그 확인
tail -f logs/application.log
```

### 시스템 모니터링:
```bash
# 메모리 사용량
free -h

# 디스크 사용량
df -h

# 프로세스 확인
ps aux | grep java

# 포트 확인
netstat -tlnp | grep :8080
```

---

## 🔧 문제 해결

### 자주 발생하는 문제:

1. **포트 8080이 열리지 않음**
   - 보안 그룹에서 8080 포트 추가 확인
   - 방화벽 설정: `sudo ufw allow 8080`

2. **데이터베이스 연결 실패**
   - PostgreSQL 서버 상태 확인
   - 네트워크 보안 그룹 설정 확인

3. **OutOfMemory 에러**
   - 인스턴스 타입을 t3.small 이상으로 변경
   - JVM 힙 메모리 설정: `-Xmx512m`

4. **애플리케이션 응답 없음**
   - 로그 확인: `tail -f logs/application.log`
   - 프로세스 확인: `./status.sh`

---

## 🔒 보안 권장사항

1. **SSH 키 관리**:
   ```bash
   chmod 400 your-key.pem
   ```

2. **보안 그룹 최소화**:
   - SSH는 특정 IP만 허용
   - 개발 완료 후 불필요한 포트 차단

3. **정기 업데이트**:
   ```bash
   sudo apt update && sudo apt upgrade -y
   ```

---

## 📞 지원

문제가 발생하면:
1. 로그 파일 확인: `/opt/didyouknow/logs/application.log`
2. 시스템 로그 확인: `sudo journalctl -f`
3. GitHub Issues에 문제 보고 