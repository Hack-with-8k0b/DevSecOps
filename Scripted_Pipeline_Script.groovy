node{
    stage('Getting the Source Code'){
        git branch: 'main', url: 'https://github.com/Hack-with-8k0b/DevSecOps.git'
    }
    stage("Building Docker image"){
        sh 'docker image build -t $JOB_NAME:v1.$BUILD_ID .'
        sh 'docker image tag  $JOB_NAME:v1.$BUILD_ID nbharathkumara/$JOB_NAME:v1.$BUILD_ID'
        sh 'docker image tag  $JOB_NAME:v1.$BUILD_ID nbharathkumara/$JOB_NAME:latest'
    }
    stage('Pushing into Docker hub'){
        withCredentials([string(credentialsId: 'dockerpass', variable: 'dockerpassword')]) {
            sh 'docker login -u nbharathkumara -p ${dockerpassword}'
            sh 'docker image push nbharathkumara/$JOB_NAME:v1.$BUILD_ID'
            sh 'docker image push nbharathkumara/$JOB_NAME:latest'
            sh 'docker image rmi $JOB_NAME:v1.$BUILD_ID nbharathkumara/$JOB_NAME:v1.$BUILD_ID nbharathkumara/$JOB_NAME:latest '
}
    }
    stage('Deployingment on the server'){
        def dockerrun = 'docker run -p 8000:80 -d --name bkob-server nbharathkumara/$JOB_NAME:latest'
        def dockerrm = 'docker container rm -f bkob-server'
        def dockerimagerm = 'docker image rmi nbharathkumara/$JOB_NAME'
        sshagent(['newsshpassword']) {
        sh "ssh -o StrictHostKeyChecking=no ec2-user@13.233.78.186 ${dockerrm}"
        sh "ssh -o StrictHostKeyChecking=no ec2-user@13.233.78.186 ${dockerimagerm}"
        sh "ssh -o StrictHostKeyChecking=no ec2-user@13.233.78.186 ${dockerrun}"
}
    }
}