pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    environment {
        DOCKER_USER = 'lakshann'
        PATH = "/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin"
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
                docker build -t $DOCKER_USER/inventory-service:$BUILD_NUMBER ./inventory-service
                docker build -t $DOCKER_USER/booking-service:$BUILD_NUMBER ./booking-service
                docker build -t $DOCKER_USER/payment-service:$BUILD_NUMBER ./payment-service
                docker build -t $DOCKER_USER/notification-service:$BUILD_NUMBER ./notification-service
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

                    docker push $DOCKER_USER/inventory-service:$BUILD_NUMBER
                    docker push $DOCKER_USER/booking-service:$BUILD_NUMBER
                    docker push $DOCKER_USER/payment-service:$BUILD_NUMBER
                    docker push $DOCKER_USER/notification-service:$BUILD_NUMBER
                    '''
                }
            }
        }

        stage('Docker Deploy') {
            steps {
                sh '''
                echo "Stopping manually started services before Docker deploy..."

                kill $(cat inventory.pid) || true
                kill $(cat booking.pid) || true
                kill $(cat payment.pid) || true
                kill $(cat notification.pid) || true

                sleep 10

                docker rm -f inventory-container booking-container payment-container notification-container || true

                docker run -d -p 8081:8081 --name inventory-container $DOCKER_USER/inventory-service:$BUILD_NUMBER
                docker run -d -p 8082:8082 --name booking-container $DOCKER_USER/booking-service:$BUILD_NUMBER
                docker run -d -p 8083:8083 --name payment-container $DOCKER_USER/payment-service:$BUILD_NUMBER
                docker run -d -p 8084:8084 --name notification-container $DOCKER_USER/notification-service:$BUILD_NUMBER
                '''
            }
        }
    }

    post {
        always {

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