def call(String sonarTokenCredID) {
    withCredentials([string(credentialsId: sonarTokenCredID, variable: 'SONAR_TOKEN')]) {
        sh '''
            mvn verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
                -Dsonar.projectKey=Anas990978_Ivolve_Training \
                -Dsonar.token=$SONAR_TOKEN
        '''
    }
}