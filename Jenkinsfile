pipeline {
    agent any

    stages {
        stage("git checkout") {
            steps {
                script {
                    env.BRANCH = env.GIT_BRANCH.split("/")[1]
                    env.PROFILE = "master".equals(env.BRANCH) ? "prod" : env.BRANCH
                }
                deleteDir()
                echo "INFO: checkout $BRANCH branch from github"
                git branch: env.BRANCH, url: "https://github.com/xaero31/smoke_cloud"
            }
        }

        stage("prepare version tag") {
            steps {
                script {
                    if ("dev".equals(env.BRANCH)) { // todo change to prod
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
                        }
                    } else {
                        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm"))
                        env.RELEASE_VERSION_TAG = env.GIT_COMMIT.substring(0, 10) + "_" + currentDate
                        buildName "build:$RELEASE_VERSION_TAG"
                    }
                    echo "INFO: prepared $BRANCH tag '$RELEASE_VERSION_TAG'"
                }
            }
        }

        stage("unit tests") {
            steps {
                sh "./gradlew test --no-daemon"
            }
        }

        stage("gradle build") {
            steps {
                echo "INFO: gradle build $RELEASE_VERSION_TAG version"
                sh "./gradlew clean build --no-daemon"
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
                    if ("master".equals(env.BRANCH)) { // todo change to dev
                        sh "docker build -t smoke-cloud:dev ."
                    }

                    if ("dev".equals(env.BRANCH)) { // todo change to master
                        sh "docker build -t smoke-cloud:$RELEASE_VERSION_TAG --build-arg " +
                           "JAR_NAME=smoke-cloud-$RELEASE_VERSION_TAG ."
                    }
                }
            }
        }

        stage("helm deploy new image") {
            steps {
                sh "echo helm deploy step"
  //              sh "helm upgrade --install --atomic dev-smoke-cloud ./helm/dev --namespace dev" // todo add params later
            }
        }

        stage("push version tag to git") {
            steps {
                withCredentials($class: "UsernamePasswordMultiBinding", credentialsId: "github",
                                usernameVariable: "GIT_USERNAME", passwordVariable: "GIT_PASSWORD") {
                    script {
                        if ("dev".equals(env.BRANCH)) { // todo change to prod
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
    }
}