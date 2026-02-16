def call(String workDir, String ecrUri, String tag) {
    sh "docker build -t ${ecrUri}:${tag} ."
}
