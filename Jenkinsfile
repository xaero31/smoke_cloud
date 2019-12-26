pipeline {
    stages {
        stage('checkout') {
            deleteDir()
            git 'https://github.com/xaero31/smoke_cloud'
        }

        stage('unit tests') {
            sh './gradlew test --no-daemon'
        }

        stage('gradle build') {
            sh './gradlew build --no-daemon'
        }

        stage('archive jar') {
            archiveArtifacts 'build/libs/*.jar'
        }

        stage('docker build image') {
            // todo
        }
    }
}