pipeline {
    agent any

    stages {
        stage("git checkout") {
            steps {
                script {
                    env.BRANCH = env.GIT_BRANCH.split("/")[1]
                    env.spring_profiles_active = "master".equals(env.BRANCH) ? "prod" : env.BRANCH
                }
                deleteDir()
                echo "INFO: checkout $BRANCH branch from github"
                git branch: env.BRANCH, url: "https://github.com/xaero31/smoke_cloud"
            }
        }

        stage("prepare version tag") {
            steps {
                script {
                    if ("master".equals(env.BRANCH)) {
                        String latestTag = sh(returnStdout: true, script: "git tag --sort=-creatordate | head -n 1").trim()
                        if (latestTag.length() == 0) {
                            env.RELEASE_VERSION_TAG = "1.1.1"
                        } else {
                            String[] versionParts = latestTag.split("\\.")
                            int[] intVersionParts = new int[versionParts.length]

                            for (int i = 0; i < intVersionParts.length; i++) {
                                intVersionParts[i] = Integer.parseInt(versionParts[i])
                            }

                            switch(env.RELEASE_VERSION) {
                                case "fix":
                                    intVersionParts[2]++
                                    break
                                case "minor":
                                    intVersionParts[1]++
                                    intVersionParts[2] = 1
                                    break
                                case "major":
                                    intVersionParts[0]++
                                    intVersionParts[1] = 1
                                    intVersionParts[2] = 1
                                    break
                            }

                            env.RELEASE_VERSION_TAG = intVersionParts[0] + "." +
                                                      intVersionParts[1] + "." + intVersionParts[2]
                            env.IMAGE_TAG = env.RELEASE_VERSION_TAG
                            buildName "release: $RELEASE_VERSION_TAG"
                        }
                    } else {
                        String currentDate = new Date().format("yyyy-MM-dd_HH:mm")
                        env.RELEASE_VERSION_TAG = env.GIT_COMMIT.substring(0, 10) + "_" + currentDate
                        env.IMAGE_TAG = "dev"
                        buildName "build: $RELEASE_VERSION_TAG"
                    }
                    echo "INFO: prepared $BRANCH tag '$RELEASE_VERSION_TAG'"
                }
            }
        }

        stage("gradle build") {
            steps {
                sh "./gradlew clean build --no-daemon -Dspring.profiles.active="
            }
        }

        stage("sonarqube analysis") {
            steps {
                script {
                    def sonar = tool "sonar_scanner"
                    withSonarQubeEnv("sonar_server") {
                        sh "$sonar/bin/sonar-scanner"
                    }
                }
            }
        }

        stage("sonarqube quality gate") {
            steps {
                timeout(time: 1, unit: "HOURS") {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage("archive jar") {
            steps {
                archiveArtifacts "build/libs/*.jar"
            }
        }

        stage("docker build image") {
            steps {
                script {
                    sh "docker build -t smoke-cloud:$IMAGE_TAG " +
                       "--build-arg JAR_NAME=smoke-cloud-$RELEASE_VERSION_TAG " +
                       "--build-arg spring_profiles_active=$spring_profiles_active ."
                }
            }
        }

        stage("helm deploy new image") {
            steps {
                sh "echo helm deploy step"
                sh "helm upgrade " +
                   "--install " +
                   "--atomic ${spring_profiles_active}-smoke-cloud " +
                   "--set image.tag=${IMAGE_TAG} " +
                   "-f ./helm/${spring_profiles_active} ./helm/smoke-cloud"
            }
        }

        stage("push version tag to git") {
            steps {
                withCredentials([[$class: "UsernamePasswordMultiBinding", credentialsId: "github",
                                  usernameVariable: "GIT_USERNAME", passwordVariable: "GIT_PASSWORD"]]) {
                    script {
                        if ("master".equals(env.BRANCH)) {
                            sh "git tag $RELEASE_VERSION_TAG"
                            sh "git push https://$GIT_USERNAME:$GIT_PASSWORD@github.com/xaero31/smoke_cloud --tags"
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            junit "build/test-results/test/*.xml"
            sh "docker container prune -f"
            sh "docker image prune -a -f"
        }

        failure {
            emailext attachLog: true,
            body: "Project: $JOB_NAME \nBuild: $RELEASE_VERSION_TAG \nBuild URL: $BUILD_URL",
            recipientProviders: [developers()],
            subject: "Build $RELEASE_VERSION_TAG failure. Project $JOB_NAME"
        }

        changed {
            script {
                if (currentBuild.currentResult == "SUCCESS") {
                    emailext attachLog: true,
                    body: "Project: $JOB_NAME \nBuild: $RELEASE_VERSION_TAG \nBuild URL: $BUILD_URL",
                    recipientProviders: [developers()],
                    subject: "Build $RELEASE_VERSION_TAG success. Project $JOB_NAME came back to normal"
                }
            }
        }
    }
}