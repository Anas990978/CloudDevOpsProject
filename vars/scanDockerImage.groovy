def call(String imageName) {
    sh "trivy image --severity HIGH,CRITICAL --timeout 5m ${imageName}:${env.BUILD_NUMBER}"
}
