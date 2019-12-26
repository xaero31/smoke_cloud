pipeline {
    agent any

    stages {
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