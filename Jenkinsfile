pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    stages {

        stage('Build Inventory Service') {
            steps {
                dir('inventory-service') {
                    sh 'mvn clean install'
                }
            }
        }

        stage('Build Booking Service') {
            steps {
                dir('booking-service') {
                    sh 'mvn clean install'
                }
            }
        }

        stage('Build Payment Service') {
            steps {
                dir('payment-service') {
                    sh 'mvn clean install'
                }
            }
        }

        stage('Build Notification Service') {
            steps {
                dir('notification-service') {
                    sh 'mvn clean install'
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

        // START SERVICES
        stage('Start Services') {
            steps {
                script {
                    sh '''
                    echo "Starting services..."

                    cd inventory-service && mvn spring-boot:run > inventory.log 2>&1 &
                    cd ../booking-service && mvn spring-boot:run > booking.log 2>&1 &
                    cd ../payment-service && mvn spring-boot:run > payment.log 2>&1 &

                    echo "Waiting for services to start..."
                    sleep 25
                    '''
                }
            }
        }

        // KARATE TESTS 
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