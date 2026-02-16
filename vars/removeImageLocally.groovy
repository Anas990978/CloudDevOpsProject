def call(String ecrUri, String tag) {
    sh "docker rmi ${ecrUri}:${tag} || true"
}
