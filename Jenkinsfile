pipeline {
    agent any

    stages {
        stage("git checkout") {
            steps {
                script {
                    String branch = env.GIT_BRANCH
                    env.GIT_BRANCH = branch.split("/")[branch.length - 1]
                    echo env.GIT_BRANCH
                }
                deleteDir()
                git branch: env.GIT_BRANCH, url: "https://github.com/xaero31/smoke_cloud"
            }
        }

        stage("prepare version tag") {
            steps {
                script {
                    if (env.GIT_BRANCH.equals("dev")) {
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
                    }
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
                sh "./gradlew build --no-daemon"
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
                    if ("dev".equals(env.GIT_BRANCH)) {
                        sh "docker build -t smoke-cloud:dev ."
                    }

                    if ("master".equals(env.GIT_BRANCH)) {
                        sh "docker build -t smoke-cloud:" + env.RELEASE_VERSION_TAG + " ."
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
                sh "git tag " + env.RELEASE_VERSION_TAG
                sh "git push --tags"
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