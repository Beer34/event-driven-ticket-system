pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    stages {

        stage('Build Inventory Service') {
            steps {
                dir('inventory-service') {
                    sh 'mvn clean install -DskipTests'
                }
            }
        }

        stage('Build Booking Service') {
            steps {
                dir('booking-service') {
                    sh 'mvn clean install -DskipTests'
                }
            }
        }

        stage('Build Payment Service') {
            steps {
                dir('payment-service') {
                    sh 'mvn clean install -DskipTests'
                }
            }
        }

        stage('Build Notification Service') {
            steps {
                dir('notification-service') {
                    sh 'mvn clean install -DskipTests'
                }
            }
        }

        stage('Unit Tests') {
            steps {
                dir('inventory-service') {
                    sh 'mvn test'
                }
                dir('booking-service') {
                    sh 'mvn test'
                }
                dir('payment-service') {
                    sh 'mvn test'
                }
                dir('notification-service') {
                    sh 'mvn test'
                }
            }
        }

        stage('Start Services') {
            steps {
                script {
                    sh '''
                    echo "Starting services with nohup..."

                    cd inventory-service
                    nohup mvn spring-boot:run > inventory.log 2>&1 &
                    echo $! > inventory.pid
                    cd ..

                    cd booking-service
                    nohup mvn spring-boot:run > booking.log 2>&1 &
                    echo $! > booking.pid
                    cd ..

                    cd payment-service
                    nohup mvn spring-boot:run > payment.log 2>&1 &
                    echo $! > payment.pid
                    cd ..

                    echo "Waiting for services to start..."
                    sleep 60

                    echo "Checking running ports..."
                    lsof -i :8081 || true
                    lsof -i :8082 || true
                    lsof -i :8083 || true

                    echo "===== INVENTORY LOG ====="
                    tail -n 50 inventory-service/inventory.log || true

                    echo "===== BOOKING LOG ====="
                    tail -n 50 booking-service/booking.log || true

                    echo "===== PAYMENT LOG ====="
                    tail -n 50 payment-service/payment.log || true
                    '''
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    dir('inventory-service') {
                        sh 'mvn sonar:sonar'
                    }
                    dir('booking-service') {
                        sh 'mvn sonar:sonar'
                    }
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
    }

    post {
        always {

            echo "Stopping services..."

            sh '''
            kill $(cat inventory-service/inventory.pid) || true
            kill $(cat booking-service/booking.pid) || true
            kill $(cat payment-service/payment.pid) || true
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