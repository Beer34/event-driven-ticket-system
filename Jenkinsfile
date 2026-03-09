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

        stage('Run Tests') {
            steps {
                dir('inventory-service') {
                    sh 'mvn test'
                }
                dir('booking-service') {
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

    }
}