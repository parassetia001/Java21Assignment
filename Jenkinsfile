pipeline {
    agent any

    environment {
        GIT_CREDENTIALS = 'github-credentials'  // Jenkins stored GitHub credentials
        IMAGE_NAME = "java21assignment:latest"
        SONARQUBE_URL = 'http://34.42.40.184:9000'  // SonarQube instance URL
        CONTAINER_NAME = "java21assignment-container"  // Docker container name
        APP_PORT = 8081  // Change this to 8081 to avoid conflict with the other job using 3000
    }

    stages {
        //Uncomment and configure if you want to clone from GitHub
        // stage('Clone Repository') {
        //     steps {
        //         script {
        //             git branch: 'main', credentialsId: "${GIT_CREDENTIALS}", url: 'https://github.com/parassetia001/Java21Assignment.git'
        //         }
        //     }
        // }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') { 
                    withCredentials([string(credentialsId: 'sonarqube-token', variable: 'SONARQUBE_TOKEN')]) {
                        sh '''
                            mvn clean verify sonar:sonar \
                                -Dsonar.projectKey=Java21Assignment \
                                -Dsonar.host.url=${SONARQUBE_URL} \
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

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $IMAGE_NAME .'
            }
        }

        stage('Stop and Remove Existing Container') {
            steps {
                script {
                    def running = sh(script: "docker ps -q -f name=$CONTAINER_NAME", returnStdout: true).trim()
                    if (running) {
                        sh "docker stop $CONTAINER_NAME"
                        sh "docker rm $CONTAINER_NAME"
                    }
                }
            }
        }

        stage('Run Docker Container') {
            steps {
                sh 'docker run -d -p $APP_PORT:8081 --name $CONTAINER_NAME $IMAGE_NAME'
            }
        }

        stage('Deploy with Ansible') {
            steps {
                script {
                    dir('/var/lib/jenkins/ansible_project') {  // Point to your Ansible directory
                        sh 'ansible-playbook -i inventory.ini deploy.yml'  // Ensure these files are in the directory
                    }
                }
            }
        }
    }

    post {
        always {
            // Clean up Docker images after the pipeline, to avoid disk space issues
            echo "Cleaning up Docker images..."
            sh 'docker system prune -af'

            // Clean up Maven's target directory (this might be temporary depending on your needs)
            echo "Cleaning up Maven target directory..."
            sh 'rm -rf target/'

            // Additional cleanup (logs, build artifacts, etc.) if needed
            echo "Cleaning up Jenkins workspace..."
            cleanWs()
        }
        
        success {
            echo "✅ Pipeline completed successfully!"
        }

        failure {
            echo "❌ Pipeline failed. Check logs!"
        }
    }
}
