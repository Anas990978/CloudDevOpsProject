def call(String imageName, String imageTag, String deploymentFile, String gitTokenCredId) {

  withCredentials([string(credentialsId: gitTokenCredId, variable: 'GIT_TOKEN')]) {
    sh """
      set -e
      
      git fetch origin gitops
      git checkout -B gitops origin/gitops

      # Update image in the deployment file (GitOps)
      sed -i -E "s#(^[[:space:]]*-?[[:space:]]*image:[[:space:]]*).*#\\1${imageName}:${imageTag}#g" ${deploymentFile}

      git config user.email "jenkins@local"
      git config user.name "jenkins"

      git add ${deploymentFile}

      # Commit only if there is a change
      git diff --cached --quiet || git commit -m "chore(gitops): update image to ${imageTag} [skip ci]"

      # Push back to your repo using token
      git remote set-url origin https://x-access-token:\${GIT_TOKEN}@github.com/Anas990978/CloudDevOpsProject.git
      git push origin HEAD:refs/heads/gitops


    """
  }
}
