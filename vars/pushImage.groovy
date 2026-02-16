def call(String ecrUri, String tag, String region, String awsCredsId) {

    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: awsCredsId]]) {

        sh """
            aws ecr get-login-password --region ${region} \
            | docker login --username AWS --password-stdin ${ecrUri.split('/')[0]}

            docker push ${ecrUri}:${tag}
        """
    }
}
