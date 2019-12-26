pipeline {
    agent any

    stages {
        stage('unit tests') {
            steps {
                try {
                    sh './gradlew test --no-daemon'
                } finally {
                    junit 'build/test-results/test/*.xml'
                }
            }
        }

        stage('gradle build') {
            steps {
                sh './gradlew build --no-daemon'
            }
        }

        stage('archive jar') {
            steps {
                archiveArtifacts 'build/libs/*.jar'
            }
        }

        stage('docker build image') {
            steps {
                sh 'echo docker build image step'
            }
        }
    }
}