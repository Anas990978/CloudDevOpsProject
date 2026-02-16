def call(String ecrUri, String tag) {
    sh "trivy image --severity HIGH,CRITICAL --timeout 5m ${ecrUri}:${tag}"
}
