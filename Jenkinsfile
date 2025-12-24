pipeline {
    agent any

    stages {
        stage('Hello') {
            steps {
                echo 'Pipeline started!'
                echo "Build number: ${env.BUILD_NUMBER}"
                echo "Building on: ${env.NODE_NAME}"
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
