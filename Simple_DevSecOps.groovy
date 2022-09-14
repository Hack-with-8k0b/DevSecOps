node{
    stage('Getting the Source Code'){
        git branch: 'main', url: 'https://github.com/Hack-with-8k0b/DevSecOps.git'
    }
    stage("Building Docker image"){
        sh 'docker image build -t $JOB_NAME:v1.$BUILD_ID .'
        sh 'docker image tag  $JOB_NAME:v1.$BUILD_ID nbharathkumara/$JOB_NAME:v1.$BUILD_ID'
        sh 'docker image tag  $JOB_NAME:v1.$BUILD_ID nbharathkumara/$JOB_NAME:latest'
    }
}