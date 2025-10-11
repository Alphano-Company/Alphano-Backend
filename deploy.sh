#!/bin/bash

# GitHub Actions에서 전달받은 이미지 태그 (첫 번째 인자)
IMAGE_TAG=$1
ECR_REGISTRY=$2
ECR_REPOSITORY=$3

# 1. 현재 실행 중인 Blue 컨테이너가 있는지 확인
EXISTING_BLUE=$(docker ps --filter "name=web-blue" --format "{{.ID}}")

if [ -z "$EXISTING_BLUE" ]; then
    # Blue가 없으면 -> 현재 Green이 실행 중이므로 -> Target은 Blue
    TARGET_ENV="blue"
    CURRENT_ENV="green"
    TARGET_PORT=8081
else
    # Blue가 있으면 -> 현재 Blue가 실행 중이므로 -> Target은 Green
    TARGET_ENV="green"
    CURRENT_ENV="blue"
    TARGET_PORT=8082
fi

echo "### Deploying to $TARGET_ENV"

# 2. 새로운 버전의 Docker 이미지 Pull
docker pull $ECR_REGISTRY/$ECR_REPOSITORY:$IMAGE_TAG

# 3. 새로운 환경(Target)으로 docker-compose 실행
# docker-compose.yml 파일에서 사용할 수 있도록 환경 변수로 이미지 정보 내보내기
export IMAGE_TAG=$IMAGE_TAG
export ECR_REGISTRY=$ECR_REGISTRY
export ECR_REPOSITORY=$ECR_REPOSITORY

docker-compose -f docker-compose.${TARGET_ENV}.yml up -d

# 4. 새로운 컨테이너가 정상적으로 실행되었는지 15초간 확인 (Health Check)
echo "### Health check for new version..."
for i in {1..15}; do
    sleep 1
    HEALTH_CHECK_URL="http://localhost:${TARGET_PORT}/health" # Health Check 엔드포인트
    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" $HEALTH_CHECK_URL)

    if [ $RESPONSE_CODE -eq 200 ]; then
        echo "### Health check success (Code: $RESPONSE_CODE)"

        # 5. Nginx 설정 변경 및 리로드 (트래픽 전환)
        echo "set \$service_url http://${TARGET_ENV}:${TARGET_PORT};" | sudo tee /home/ubuntu/app/nginx/service-url.inc
        sudo nginx -s reload

        echo "### Switched to $TARGET_ENV"

        # 6. 이전 환경(Current) 컨테이너 종료
        if [ ! -z "$CURRENT_ENV" ]; then
            docker-compose -f docker-compose.${CURRENT_ENV}.yml down
            echo "### Shutdown $CURRENT_ENV"
        fi

        exit 0
    fi
    echo "Health check retrying... ($i/15)"
done

echo "### Health check failed"
docker-compose -f docker-compose.${TARGET_ENV}.yml down
exit 1