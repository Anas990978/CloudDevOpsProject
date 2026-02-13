def call(String imageName, String deploymentFile, String apiServerCredID, String tokenCredID) {
    sh "sed -i 's|image: .*|image: ${imageName}:${BUILD_NUMBER}|' ${deploymentFile}"
    withCredentials([
        string(credentialsId: tokenCredID, variable: 'TOKEN'),
        string(credentialsId: apiServerCredID, variable: 'API_SERVER')
    ]) {
        sh "kubectl apply -f ${deploymentFile} --server=\$API_SERVER --token=\$TOKEN --insecure-skip-tls-verify=true"
    }
}
