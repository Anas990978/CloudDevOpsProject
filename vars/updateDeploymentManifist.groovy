def call(String imageName, String imageTag, String deploymentFile, String gitTokenCredId) {

  withCredentials([string(credentialsId: gitTokenCredId, variable: 'GIT_TOKEN')]) {
    sh """
      set -e

      # Update image in the deployment file (GitOps)
      sed -i 's|^\\(\\s*image:\\s*\\).*|\\1${imageName}:${imageTag}|' ${deploymentFile}

      git config user.email "jenkins@local"
      git config user.name "jenkins"

      git add ${deploymentFile}

      # Commit only if there is a change
      git diff --cached --quiet || git commit -m "chore(gitops): update image to ${imageTag} [skip ci]"

      # Push back to your repo using token
      git remote set-url origin https://x-access-token:\${GIT_TOKEN}@github.com/Anas990978/CloudDevOpsProject.git
      git push origin HEAD:\${BRANCH_NAME}
    """
  }
}
