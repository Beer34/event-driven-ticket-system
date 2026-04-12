pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    environment {
        DOCKER_USER = 'lakshann'
    }

    stages {

        stage('Build Services') {
            steps {
                sh '''
                mvn -f inventory-service/pom.xml clean install -DskipTests
                mvn -f booking-service/pom.xml clean install -DskipTests
                mvn -f payment-service/pom.xml clean install -DskipTests
                mvn -f notification-service/pom.xml clean install -DskipTests
                '''
            }
        }

        stage('Unit Tests') {
            steps {
                sh '''
                mvn -f inventory-service/pom.xml test
                mvn -f booking-service/pom.xml test
                mvn -f payment-service/pom.xml test
                mvn -f notification-service/pom.xml test
                '''
            }
        }

        stage('Start Services') {
            steps {
                sh '''
                echo "Starting services..."

                nohup mvn -f inventory-service/pom.xml spring-boot:run > inventory.log 2>&1 &
                echo $! > inventory.pid

                nohup mvn -f booking-service/pom.xml spring-boot:run > booking.log 2>&1 &
                echo $! > booking.pid

                nohup mvn -f payment-service/pom.xml spring-boot:run > payment.log 2>&1 &
                echo $! > payment.pid

                nohup mvn -f notification-service/pom.xml spring-boot:run > notification.log 2>&1 &
                echo $! > notification.pid

                echo "Waiting for services to be ready..."
                sleep 60
                '''
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh '''
                    mvn -f inventory-service/pom.xml sonar:sonar -Dsonar.projectKey=inventory-service
                    mvn -f booking-service/pom.xml sonar:sonar -Dsonar.projectKey=booking-service
                    mvn -f payment-service/pom.xml sonar:sonar -Dsonar.projectKey=payment-service
                    mvn -f notification-service/pom.xml sonar:sonar -Dsonar.projectKey=notification-service
                    '''
                }
            }
        }

        stage('Integration Tests - Karate') {
            steps {
                dir('karate-tests') {
                    sh '''
                    export JAVA_HOME=$(/usr/libexec/java_home -v 17)
                    mvn clean test
                    '''
                }
            }
        }

        stage('Docker Build Images') {
            steps {
                sh '''
                docker build -t $DOCKER_USER/inventory-service:latest ./inventory-service
                docker build -t $DOCKER_USER/booking-service:latest ./booking-service
                docker build -t $DOCKER_USER/payment-service:latest ./payment-service
                docker build -t $DOCKER_USER/notification-service:latest ./notification-service
                '''
            }
        }

        stage('Docker Push Images') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USERNAME',
                    passwordVariable: 'DOCKER_PASSWORD'
                )]) {
                    sh '''
                    echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin

                    docker push $DOCKER_USER/inventory-service:latest
                    docker push $DOCKER_USER/booking-service:latest
                    docker push $DOCKER_USER/payment-service:latest
                    docker push $DOCKER_USER/notification-service:latest
                    '''
                }
            }
        }

        stage('Docker Deploy') {
            steps {
                sh '''
                docker compose down || true
                docker compose up -d
                '''
            }
        }
    }

    post {
        always {
            echo "Stopping manually started services..."

            sh '''
            kill $(cat inventory.pid) || true
            kill $(cat booking.pid) || true
            kill $(cat payment.pid) || true
            kill $(cat notification.pid) || true
            '''

            junit '**/target/surefire-reports/*.xml'

            jacoco execPattern: '**/target/jacoco.exec',
                   classPattern: '**/target/classes',
                   sourcePattern: '**/src/main/java'

            publishHTML([
                reportDir: 'karate-tests/target/karate-reports',
                reportFiles: 'karate-summary.html',
                reportName: 'Karate Test Report',
                keepAll: true,
                alwaysLinkToLastBuild: true,
                allowMissing: false
            ])
        }
    }
}