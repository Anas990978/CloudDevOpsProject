# CloudDevOpsProject

End-to-end DevOps pipeline for a containerized app: Docker build, Kubernetes manifests, Terraform AWS infra, Ansible configuration, Jenkins CI, and ArgoCD CD.

## Repository Setup (GitHub)

1. Create a new GitHub repository named `CloudDevOpsProject` and initialize it with a README.
2. Add the remote and push this project:

```bash
git init
git remote add origin https://github.com/Anas990978/CloudDevOpsProject.git
git add .
git commit -m "Initial commit"
git push -u origin main
```

Deliverable: repository URL (replace with your actual URL).

## Application Source

The application source is from:

- https://github.com/IbrahimAdel15/FinalProject.git

This repo already includes the app under `App/FinalProject/` with a Dockerfile at `App/Dockerfile`.

## 1. Containerization with Docker

**Files**
- `App/Dockerfile`
- `App/FinalProject/`

**Build and run locally**

```bash
cd App
# Build image
docker build -t finalproject:latest .
```

**Commands used**

```bash
cd App
docker build -t finalproj-app .
docker run -d --name final-proj-cont -p 5001:5000 finalproj-app:latest
docker rm final-proj-cont
docker rmi finalproj-app:latest
```


Deliverable: Dockerfile committed.

## 2. Kubernetes Orchestration

**Files**
- `K8s/ns.yaml` (namespace)
- `K8s/deployment.yaml` (deployment)
- `K8s/service.yaml` (service)

**Apply manifests**

```bash
kubectl apply -f K8s/ns.yaml
```

**Commands used**

```bash
kubectl create deployment ivolve-proj \
  --image=637423620989.dkr.ecr.us-east-1.amazonaws.com/ivolve-app:${BUILD_NUMBER} \
  -n ivolve \
  --dry-run=client -o yaml > K8s/deployment.yaml
kubectl expose deployment ivolve-proj \
  --type=LoadBalancer \
  --port=80 \
  --target-port=5000 \
  -n ivolve \
  --dry-run=client -o yaml > K8s/service.yaml
```

Deliverable: required YAML committed.

## 3. Infrastructure Provisioning with Terraform (AWS)

**Structure**
- `Terraform/modules/network` (VPC, subnets, IGW, NACL)
- `Terraform/modules/jenkins` (EC2 Jenkins + SG)
- `Terraform/modules/eks` (EKS cluster)
- `Terraform/modules/ecr` (ECR registry)
- `Terraform/Backend` (S3 backend)
- `Terraform/infra` (root module)


**Commands used**

```bash
cd Terraform/Backend
terraform init
terraform apply -auto-approve
cd ../infra
terraform init
terraform apply -auto-approve
aws eks update-kubeconfig --region us-east-1 --name ivolve-proj-eks
```

Deliverables: Terraform scripts committed (VPC, subnets, IGW, NACL, EC2 Jenkins, S3 backend, CloudWatch, modules).

## 4. Configuration Management with Ansible

**Files**
- `Ansible/playbook.yaml`
- `Ansible/inventory.aws_ec2.yml` (dynamic inventory)
- `Ansible/roles/common-tools`
- `Ansible/roles/docker`
- `Ansible/roles/jenkins`
- `Ansible/roles/trivy`

**Commands used**

```bash
ansible-galaxy collection install amazon.aws
ansible-inventory -i inventory.aws_ec2.yml --graph
ansible-playbook -i inventory.aws_ec2.yml playbook.yaml
```

Deliverable: playbooks, roles, dynamic inventory committed.

## 5. Continuous Integration with Jenkins

**Files**
- `Jenkins/jenkinsfile`
- `vars/` (shared library: build, scan, push, remove, update manifests)

**Pipeline stages**
- Build Image
- Scan Image
- Push Image
- Delete Image Locally
- Update Manifests
- Push Manifests

Deliverable: Jenkinsfile and shared library committed.

## 6. Continuous Deployment with ArgoCD

**Files**
- `ArgoCd/argo-cd-ns.yaml`

**Commands used**

```bash
kubectl create ns argocd -o yaml > ArgoCd/argo-cd-ns.yaml
kubectl apply -f ArgoCd/argo-cd-ns.yaml
kubectl apply -n argocd \
  -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
kubectl -n argocd patch svc argocd-server -p '{"spec": {"type": "LoadBalancer"}}'
kubectl -n argocd get secret argocd-initial-admin-secret \
  -o jsonpath="{.data.password}" | base64 -d; echo
```

Create an ArgoCD Application pointing to this repo and the `K8s/` path. Commit the Application manifest when ready.

Deliverable: ArgoCD Application manifest committed.

## 7. Documentation

This README provides:
- Setup instructions
- Architecture overview

## Architecture Overview

1. Terraform provisions AWS infra: VPC, subnets, IGW/NACL, EC2 Jenkins, EKS, ECR, CloudWatch.
2. Ansible configures EC2 instances (tools, Docker, Jenkins, Trivy).
3. Jenkins CI builds/scans/pushes the Docker image and updates K8s manifests.
4. Kubernetes runs the application in the `iVolve` namespace.
5. ArgoCD syncs and deploys changes from Git to the cluster.

## Notes

- Jenkins builds and pushes images to `637423620989.dkr.ecr.us-east-1.amazonaws.com/ivolve-app` with tag `${BUILD_NUMBER}`.
- Ensure AWS credentials are configured for Terraform and Ansible (if using AWS dynamic inventory).

## Author
**Anas Tarek**
