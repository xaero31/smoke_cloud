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
            steps {
                archiveArtifacts 'build/libs/*.jar'
            }
        }

        stage('docker build image') {
            steps {
                sh 'docker build -t smoke-cloud:new .'
            }
        }

        stage('helm deploy new image') {
            steps {
                sh 'echo helm deploy step'
  //              sh 'helm upgrade --install --atomic dev-smoke-cloud ./helm/dev --namespace dev' // todo add params later
            }
        }
    }

    post {
        always {
            junit 'build/test-results/test/*.xml'
            sh 'docker container prune -f'
            sh 'docker image prune -a -f'
        }
    }
}