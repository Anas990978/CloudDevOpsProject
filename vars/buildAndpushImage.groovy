def call(String imageName, String dockerhubCredID){

    sh "docker build -t ${imageName}:${env.BUILD_NUMBER} ."
    withCredentials([usernamePassword(
        credentialsId: dockerhubCredID, 
        usernameVariable: 'DOCKERHUB_USERNAME', 
        passwordVariable: 'DOCKERHUB_PASSWORD'
    )]) {
        sh """
            docker login -u $DOCKERHUB_USERNAME -p $DOCKERHUB_PASSWORD
            docker push ${imageName}:${env.BUILD_NUMBER}
        """
    }
    sh "docker rmi ${imageName}:${env.BUILD_NUMBER} || true"

} 