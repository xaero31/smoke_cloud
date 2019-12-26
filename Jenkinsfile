pipeline {
    agent any

    stages {
        stage('checkout') {
            steps {
                deleteDir()
                git 'https://github.com/xaero31/smoke_cloud'
            }
        }

        stage('unit tests') {
            steps {
                sh './gradlew test --no-daemon'
            }
        }

        stage('gradle build') {
            steps {
                sh './gradlew build --no-daemon'
            }
        }

        stage('docker build image') {
            steps {
                sh 'echo docker build image step'
            }
        }
    }
}