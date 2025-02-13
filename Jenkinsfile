pipeline {
    agent any

    // Docker Hub 자격증명 및 이미지 정보 (Jenkins의 Credentials에 등록되어 있어야 합니다)
    environment {
        DOCKERHUB_CREDENTIALS = credentials('docker-hub') // Jenkins에 등록된 Docker Hub 크리덴셜 ID
        IMAGE_NAME = 'na0th/na0th'
        IMAGE_TAG = '1.0.0' // 필요에 따라 버전을 동적으로 지정할 수도 있음
    }

    stages {
        stage('Git Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build JAR with Gradle') {  // ✅ JAR 빌드 추가
            steps {
                script {
                    echo "🛠️ Gradle 실행 권한 부여"
                    sh "chmod +x ./gradlew"  // ✅ 실행 권한 추가
                    
                    echo "🛠️ Gradle을 사용하여 JAR 빌드"
                    sh "./gradlew clean build -x test" // 테스트 제외하고 빌드

                    echo "📂 빌드된 파일 확인:"
                    sh "ls -al build/libs/"
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    echo "빌드 중: ${IMAGE_NAME}:${IMAGE_TAG}"
                    sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    echo "Docker Hub에 로그인 중"
                    // 환경 변수 DOCKERHUB_CREDENTIALS_USR, DOCKERHUB_CREDENTIALS_PSW는 credentials()로부터 자동으로 설정됨
                    withCredentials([usernamePassword(credentialsId: 'docker', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh "echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin"
                    }
                    echo "이미지 푸시 중: ${IMAGE_NAME}:${IMAGE_TAG}"
                    sh "docker push ${IMAGE_NAME}:${IMAGE_TAG}"
                }
            }
        }


        stage('Deploy to EC2') {
            steps {
                sshagent(['ec2-ssh']) {
                    sh """
                    echo "🚀 배포 스크립트 업로드 중..."
                    scp -o StrictHostKeyChecking=no ./scripts/deploy.sh ubuntu@54.180.87.11:/home/ubuntu/deploy.sh

                    ssh -tt ubuntu@54.180.87.11 -o StrictHostKeyChecking=no << 'EOF'
                    chmod +x /home/ubuntu/deploy.sh
                    /home/ubuntu/deploy.sh
                    EOF
                    """
                }
            }
        }
    }

    post {
        success {
            echo '🚀 배포 성공! happy'
        }
        failure {
            echo '❌ 배포 실패! sad..'
        }
    }
}
