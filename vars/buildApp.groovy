def call (){
    sh 'echo "Running unit tests..."'
    sh 'mvn test'
    sh 'mvn package'
}   