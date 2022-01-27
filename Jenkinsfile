pipeline {
  agent any
  stages {
    stage('Build Gradle') {
      steps {
        sh './gradlew clean build'
      }
    }
    stage('Testing') {
      steps {
        sh ' ./gradlew test -warning-mode all'
      }
    }
    stage('Build Docker image') {
      steps {
        sh 'docker rmi -f bf_telegram-api'
        sh 'docker build -t bf_telegram-api .'
      }
    }
    stage('Create Docker swarm service') {
      steps {
        sh './scripts/DockerServiceUp.sh'
      }
    }
  }
}
