pipeline{

    agent any

    stages{
        stage('Getting Source Code from Git'){
            steps{
                git branch: 'main', url: 'https://github.com/Hack-with-8k0b/DevSecOps.git'
            }
        }

        stage('Building Docker Images'){
            steps{
                sh 'docker image build -t $JOB_NAME:v1.$BUILD_ID .'
                sh 'docker image tag $JOB_NAME:v1.$BUILD_ID nbharathkumara/$JOB_NAME:v1.$BUILD_ID'
                sh 'docker image tag $JOB_NAME:v1.$BUILD_ID nbharathkumara/$JOB_NAME:latest'
            }
        }
        stage('Pushing into DockerHub'){
            steps{
            withCredentials([string(credentialsId: 'dockerpass', variable: 'dockerpassword')]) {
            sh 'docker login -u nbharathkumara -p ${dockerpassword}'
            sh 'docker image push nbharathkumara/$JOB_NAME:v1.$BUILD_ID'
            sh 'docker image push nbharathkumara/$JOB_NAME:latest'
            sh 'docker image rmi $JOB_NAME:v1.$BUILD_ID nbharathkumara/$JOB_NAME:v1.$BUILD_ID nbharathkumara/$JOB_NAME:latest '

            }

        }
    }
}