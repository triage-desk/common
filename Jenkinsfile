pipeline {
    agent {
        label 'oracle-host-agent'
    }

    stages {
        stage('Build and Test') {
            when {
                anyOf {
                    changeRequest()
                    branch 'main'
                }
            }
            stages {
                stage('Lint and Format') {
                    steps {
                        sh 'chmod +x ./mvnw'
                        sh './mvnw spotless:check checkstyle:check'
                    }
                }

                stage('Unit Tests') {
                    steps {
                        sh './mvnw clean test'
                    }
                }

                stage('SonarQube Analysis') {
                    steps {
                        withSonarQubeEnv('SonarQube') {
                            withEnv(["SONAR_USER_HOME=${env.WORKSPACE}/.sonar"]) {
                                sh 'rm -rf "${SONAR_USER_HOME}/cache" || true'
                                // Uses full plugin coordinate to avoid "No plugin found" error
                                sh './mvnw org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=triage-desk-common'
                            }
                        }

                        timeout(time: 5, unit: 'MINUTES') {
                            waitForQualityGate abortPipeline: true
                        }
                    }
                }

                stage('Deploy to GitHub Packages') {
                    when {
                        branch 'main'
                    }
                    environment {
                        GITHUB_CREDS = credentials('github-package-creds')
                    }
                    steps {
                        // Using triple single-quotes (''') so the shell safely handles the secret
                        sh '''
                            cat << EOF > settings.xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.2.0">
    <servers>
        <server>
            <id>github</id>
            <username>${GITHUB_CREDS_USR}</username>
            <password>${GITHUB_CREDS_PSW}</password>
        </server>
    </servers>
</settings>
EOF
                        '''

                        sh './mvnw deploy -DskipTests -s settings.xml'
                    }
                    post {
                        always {
                            sh 'rm -f settings.xml || true'
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/*-reports/*.xml'
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Check the logs.'

            mail to: 'eyad.m.sharkawy@gmail.com',
            subject: "FAILED: Job '${env.JOB_NAME}' [Build #${env.BUILD_NUMBER}]",
            body: "Your Jenkins pipeline failed on branch '${env.BRANCH_NAME}'. Check the logs at ${env.BUILD_URL}"
        }
    }
}
