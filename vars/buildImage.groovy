def call(String workDir, String ecrUri, String tag) {
    dir(workDir) {
        sh "docker build -t ${ecrUri}:${tag} ."
    }
}
