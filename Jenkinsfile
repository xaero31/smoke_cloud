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

        stage('archive jar') {
            archiveArtifacts 'build/libs/*.jar'
        }

        stage('docker build image') {
            steps {
                sh 'echo docker build image step'
            }
        }
    }
}