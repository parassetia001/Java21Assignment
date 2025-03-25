pipeline {
    agent any

    environment {
        GIT_CREDENTIALS = 'github-credentials'  // Jenkins stored GitHub credentials
        DOCKER_USER = 'parassetia001'           // Docker Hub username
        IMAGE_NAME = "${DOCKER_USER}/java21assignment:latest"
        SONARQUBE_URL = 'http://localhost:9000'  // SonarQube instance URL
    }

    stages {
        stage('Clone Repository') {
            steps {
                script {
                    git branch: 'main', credentialsId: "${GIT_CREDENTIALS}", url: 'https://github.com/parassetia001/Java21Assignment.git'
                }
            }
        }

stage('SonarQube Analysis') {
    steps {
        withSonarQubeEnv('SonarQube') { // Use your SonarQube configuration name
            withCredentials([string(credentialsId: 'sonarqube-token', variable: 'SONARQUBE_TOKEN')]) {
                sh '''
                    mvn clean verify sonar:sonar \
                        -Dsonar.projectKey=Java21Assignment \
                        -Dsonar.host.url=${SONAR_HOST_URL} \
                        -Dsonar.login=${SONARQUBE_TOKEN}
                '''
            }
        }
    }
}

        stage('Quality Gate Check') {
            steps {
                script {
                    timeout(time: 1, unit: 'MINUTES') {
                        def qualityGate = waitForQualityGate()
                        if (qualityGate.status != 'OK') {
                            error("❌ Quality Gate failed! Status: ${qualityGate.status}")
                        }
                    }
                }
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Run Unit Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Build & Push Docker Image') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh """
                            docker login -u $DOCKER_USER -p $DOCKER_PASS
                            docker build -t $IMAGE_NAME .
                            docker push $IMAGE_NAME
                        """
                    }
                }
            }
        }

        stage('Deploy with Ansible') {
            steps {
                script {
                    sh "docker exec ansible-container ansible-playbook -i /ansible/inventory.ini /ansible/deploy.yml"
                }
            }
        }
    }

    post {
        success {
            echo "✅ Pipeline completed successfully!"
        }
        failure {
            echo "❌ Pipeline failed. Check logs!"
        }
    }
}
