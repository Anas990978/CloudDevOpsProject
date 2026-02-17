def call(String imageName, String imageTag, String deploymentFile, String gitTokenCredId) {

  withCredentials([string(credentialsId: gitTokenCredId, variable: 'GIT_TOKEN')]) {
    sh """
      set -euo pipefail

      echo "Updating manifest: ${deploymentFile}"
      echo "Setting image to: ${imageName}:${imageTag}"

      if [ ! -f "${deploymentFile}" ]; then
        echo "ERROR: ${deploymentFile} not found!"
        exit 1
      fi

      echo "----- BEFORE -----"
      grep -n "image:" "${deploymentFile}" || true

      # Update image in the deployment file (GitOps) - supports indentation
      sed -i -E "s#(^[[:space:]]*image:[[:space:]]*).*#\\\\1${imageName}:${imageTag}#g" "${deploymentFile}"

      echo "----- AFTER ------"
      grep -n "image:" "${deploymentFile}" || true

      git config user.email "jenkins@local"
      git config user.name "jenkins"

      git add "${deploymentFile}"

      # Commit only if there is a change
      if git diff --cached --quiet; then
        echo "No changes to commit (image already ${imageName}:${imageTag})"
      else
        git commit -m "chore(gitops): update image to ${imageTag} [skip ci]"
      fi

      # Push back to your repo using token
      git remote set-url origin https://x-access-token:\${GIT_TOKEN}@github.com/Anas990978/CloudDevOpsProject.git
      git push origin HEAD:main
    """
  }
}
