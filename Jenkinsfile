pipeline {
    agent any

    tools {
        jdk 'Java17'     // use the JDK name from "Global Tool Configuration"
        maven 'Maven3'   // use the Maven name from "Global Tool Configuration"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/iamsp7/MicroService-Project.git'
            }
        }

        stage('Build') {
            steps {
                script {
                    // ✅ fixed signup path
                    def modules = ['gateway/gateway', 'signin', 'signup/signup']
                    for (m in modules) {
                        dir(m) {
                            sh 'mvn clean package -DskipTests'
                        }
                    }
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    // ✅ fixed signup path
                    def modules = ['gateway/gateway', 'signin', 'signup/signup']
                    for (m in modules) {
                        dir(m) {
                            sh 'mvn test'
                        }
                    }
                }
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package Artifact') {
            steps {
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            }
        }

        stage('Deploy') {
            steps {
                echo "🚀 Deploying microservices..."
                script {
                    def modules = ['gateway/gateway', 'signin', 'signup/signup']
                    for (m in modules) {
                        dir(m) {
                            sh '''
                            nohup java -jar target/*.jar > app.log 2>&1 &
                            '''
                        }
                    }
                }
            }
        }
    }

    post {
        success {
            echo '✅ Build & deploy successful!'
        }
        failure {
            echo '❌ Build failed!'
        }
    }
}
