def dockerrm = 'docker container rm -f bkob-server'
def dockerimagerm = 'docker image rmi nbharathkumara/$JOB_NAME'
def dockerrun = 'docker run -p 8000:80 -d --name bkob-server nbharathkumara/$JOB_NAME:latest'

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
        stage('Deploying the Webserver'){
            steps{
            sshagent(['newsshpassword']) {
                script{
                    if (sh 'ssh -o StrictHostKeyChecking=no ec2-user@65.0.169.57 docker container inspect bkob-server') {
                        sh "ssh -o StrictHostKeyChecking=no ec2-user@65.0.169.57 ${dockerrm}"
                        sh "ssh -o StrictHostKeyChecking=no ec2-user@65.0.169.57 ${dockerrun}"
                    } else {
                        sh "ssh -o StrictHostKeyChecking=no ec2-user@65.0.169.57 ${dockerrun}"             
                    }

                }


            //sh "ssh -o StrictHostKeyChecking=no ec2-user@65.0.169.57 ${dockerrm}"
            //sh "ssh -o StrictHostKeyChecking=no ec2-user@65.0.169.57 ${dockerimagerm}"
            //sh "ssh -o StrictHostKeyChecking=no ec2-user@65.0.169.57 ${dockerrun}"
                }
            }
        }
    }
}