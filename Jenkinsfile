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

        stage('docker build image') {
            // todo
        }
    }
}