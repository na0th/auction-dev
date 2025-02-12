#!/bin/bash

# docker-compose.yml이 위치한 디렉토리로 이동 (필요한 경우)
cd /home/ubuntu/myproject || exit

echo "📦 최신 Docker 이미지 가져오기..."
docker-compose pull

echo "존재하는 컨테이너 중지"
docker compose down

echo "🔄 컨테이너 재시작..."
# 컨테이너가 실행 중이면 업데이트 적용을 위해 재시작합니다.
docker-compose up -d

echo "🧹 불필요한 이미지 정리..."
docker image prune -f

echo "✅ 배포 완료!"
