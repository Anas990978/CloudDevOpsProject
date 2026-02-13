def call(String imageName, String deploymentFile, String namespace, String apiServerCredID, String tokenCredID) {

    def imageTag = "${imageName}:${env.BRANCH_NAME}-${env.BUILD_NUMBER}"

    sh """
        sed -i 's|image: .*|image: ${imageTag}|' ${deploymentFile}
    """

    withCredentials([
        string(credentialsId: tokenCredID, variable: 'TOKEN'),
        string(credentialsId: apiServerCredID, variable: 'API_SERVER')
    ]) {

        sh """
            kubectl apply -n ${namespace} -f ${deploymentFile} \
              --server=\$API_SERVER \
              --token=\$TOKEN \
              --insecure-skip-tls-verify=true
        """
    }
}
