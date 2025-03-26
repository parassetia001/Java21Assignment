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
	//Uncomments and configure if you want to clone from GitHub
        // stage('Clone Repository') {
        //     steps {
        //         script {
        //             git branch: 'main', credentialsId: "${GIT_CREDENTIALS}", url: 'https://github.com/parassetia001/Java21Assignment.git'
        //         }
        //     }
        // }
        
        stage('Build with Maven') {
            steps {
                // Build the project and skip tests initially
                sh 'mvn clean package -DskipTests'
            }
        }        

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

        // Unit Tests Stage
        stage('Run Unit Tests') {
            steps {
                // Run unit tests
                sh 'mvn test'
            }
        }

        // Integration Tests Stage
        stage('Run Integration Tests') {
            steps {
                // Run integration tests (API, DB interactions)
                sh 'mvn verify -DskipTests=false -Dtest=IntegrationTest'
            }
        }

        // End-to-End Tests Stage
        stage('Run E2E Tests') {
            steps {
                // Run E2E tests (UI or API flow)
                sh 'mvn test -Dtest=com.example.CarServiceE2ETest'
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

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $IMAGE_NAME .'
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
