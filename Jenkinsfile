pipeline {
    agent any

    tools {
        jdk 'JDK25'  // Must match the JDK name configured in Jenkins Tools
    }

    stages {
        stage('Hello') {
            steps {
                echo 'Pipeline started!'
                echo "Build number: ${env.BUILD_NUMBER}"
                echo "Building on: ${env.NODE_NAME}"
            }
        }

        stage('Checkout') {
            steps {
                // The checkout happens automatically when using "Pipeline script from SCM"
                echo 'Code checked out from Git'
            }
        }

        stage('Build') {
            steps {
                sh './mvnw -B clean compile'
            }
        }

        stage('Test') {
            steps {
                sh './mvnw -B test'
            }
        }

        stage('Package') {
            steps {
                sh './mvnw -B package -DskipTests'
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline completed successfully!'
            echo 'Artifact: target/*.jar'
        }
        failure {
            echo '❌ Pipeline failed! Check the logs above for errors.'
        }
        always {
            echo 'Pipeline finished.'
        }
    }
}
