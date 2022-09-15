pipeline{

    agent any

    stages{
        stage('Getting Source Code from Git'){
            steps{
                git branch: 'main', url: 'https://github.com/Hack-with-8k0b/DevSecOps.git'
            }
        }

        stage('Building Docker Image'){
            steps{
                sh 'docker image build -t $JOB_NAME:v1.$BUILD_ID .'
                sh 'docker image tag $JOB_NAME:v1.$BUILD_ID nbharathkumara/$JOB_NAME:v1.$BUILD_ID'
                sh 'docker image tag $JOB_NAME:v1.$BUILD_ID nbharathkumara/$JOB_NAME:latest'
            }
        }
    }
}