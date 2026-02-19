# Flask App Deployment on AWS EKS with CI/CD, GitOps, and Infrastructure as Code

## Project Overview

This project demonstrates an end-to-end DevOps workflow for deploying a containerized Flask application to Amazon EKS using modern DevOps practices.

The system is automated and reproducible using:
- Infrastructure as Code (Terraform)
- Configuration Management (Ansible)
- Continuous Integration (Jenkins)
- Containerization (Docker)
- Container Registry (Amazon ECR)
- Kubernetes Orchestration (Amazon EKS)
- GitOps Deployment (ArgoCD)
- Security Scanning (Trivy)

---

## High-Level Architecture

<img width="3629" height="1854" alt="ivolve-proj3" src="https://github.com/user-attachments/assets/a35d126c-2e28-46c1-adb1-138ad3c15d88" />

## Technologies Used

| Category | Tools |
|----------|--------|
| Infrastructure | Terraform |
| Configuration | Ansible |
| CI/CD | Jenkins |
| Containerization | Docker |
| Registry | Amazon ECR |
| Orchestration | Amazon EKS |
| GitOps | ArgoCD |
| Security | Trivy |
| Cloud Provider | AWS |

## Table of Contents

1. Repository Setup
2. Containerization with Docker 
3. Infrastructure as Code with Terraform
5. Configuration Management with Ansible
6. Continuous Integration with Jenkins
7. Continuous Deployment with ArgoCD
8. Verification Steps
9. Repository Structure
## Docker (Build and Run)

Build locally :

```bash
cd App
docker build -t finalproject:latest .
```

## Infrastructure Provisioning (Terraform)

Terraform provisions:
- VPC & networking
- Security groups
- Jenkins EC2
- EKS cluster
- Node group
- ECR repository
- S3 backend for state

Initialize backend:

```bash
cd Terraform/Backend
terraform init
terraform apply -auto-approve
```

Apply infrastructure:

```bash
cd ../infra
terraform init
terraform apply -auto-approve
```
<img width="1245" height="787" alt="image" src="https://github.com/user-attachments/assets/4e9eff46-c51a-489c-bb46-bd4e1b24632f" />
<img width="1545" height="685" alt="image" src="https://github.com/user-attachments/assets/1fd84cfc-43b2-4cfc-8172-c4378d04b740" />
<img width="1543" height="682" alt="image" src="https://github.com/user-attachments/assets/47b73c1d-6202-422c-b626-4e903ffd06e9" />
<img width="1549" height="597" alt="image" src="https://github.com/user-attachments/assets/fc99d097-3398-44d3-aaf5-2960a06c9305" />

Update kubeconfig:

```bash
aws eks update-kubeconfig --region us-east-1 --name ivolve-proj-eks
```

## Configuration Management (Ansible)

Ansible configures the Jenkins EC2 instance with:
- Java
- Jenkins
- Docker
- AWS CLI
- Trivy

Run Ansible:

```bash
cd Ansible
ansible-galaxy collection install amazon.aws
ansible-playbook -i inventory.aws_ec2.yml playbook.yaml
```
<img width="1373" height="620" alt="image" src="https://github.com/user-attachments/assets/5a7f4b8b-47c6-4e2f-ab12-b12794efe3b4" />
<img width="1365" height="630" alt="image" src="https://github.com/user-attachments/assets/be2ac97f-bbe4-4f14-bcd0-466240896cf1" />
<img width="1368" height="280" alt="image" src="https://github.com/user-attachments/assets/8b77fbbf-0ff5-46d7-98a4-a7415f110bd1" />

## CI Pipeline (Jenkins)

The Jenkins pipeline performs:
- Build Docker image
- Security scan using Trivy
- Push image to Amazon ECR
- Update Kubernetes deployment manifest (GitOps)
- Commit and push changes

Pipeline stages:
- Build Image
- Security Scan
- Push Image
- Cleanup
- Update K8s Manifest (GitOps)
<img width="1537" height="728" alt="image" src="https://github.com/user-attachments/assets/f0178949-7dcf-42a7-90cc-11b3b2c8a6ce" />

Jenkins configuration:
- Shared library name: `shared-lib`
- AWS credentials ID: `aws-creds`
- GitHub token ID: `github-token`
- ECR URI: `637423620989.dkr.ecr.us-east-1.amazonaws.com/ivolve-app`
- Image tag: `${BUILD_NUMBER}`
- WebHook : `http://54.225.32.247:8080/github-web` to trigger the pipeline whenever the code repo changes  
<img width="1543" height="725" alt="image" src="https://github.com/user-attachments/assets/b78fbfb9-8f2f-41e5-82df-e3e4c8d03a8b" />
<img width="1564" height="733" alt="image" src="https://github.com/user-attachments/assets/aa985930-a351-4f67-abc0-50ccca4a0f80" />
<img width="1538" height="715" alt="image" src="https://github.com/user-attachments/assets/09d96d2c-0005-48e9-ab03-f0c34083fa37" />
---

## Security Scanning (Trivy)

Images are scanned for HIGH and CRITICAL vulnerabilities before pushing to ECR.

Example:

```bash
trivy image --severity HIGH,CRITICAL image:tag
```

---

## Amazon ECR

Images are stored in:

```
637423620989.dkr.ecr.us-east-1.amazonaws.com/ivolve-app:<tag>
```
<img width="1543" height="682" alt="image" src="https://github.com/user-attachments/assets/d5729b04-2d23-450f-8810-c061d93fd64c" />
 
Login command:

```bash
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 637423620989.dkr.ecr.us-east-1.amazonaws.com
```

## Kubernetes (Amazon EKS)

Kubernetes resources include:
- Namespace
- Deployment
- LoadBalancer service

Apply manifests:

```bash
kubectl apply -f K8s/ns.yaml
```

Verification:

```bash
kubectl get pods -n ivolve
kubectl get svc -n ivolve
```

---

## GitOps with ArgoCD

ArgoCD continuously monitors:
- `K8s/deployment.yaml`

When Jenkins updates the image tag:
- ArgoCD detects changes
- Syncs automatically
- Deploys new version to EKS

Benefits:
- Automatic deployments
- Git-based version control
- Easy rollback
- Declarative Kubernetes management

---

## End-to-End CI/CD Flow

1. Developer pushes code to GitHub
2. Jenkins triggered via webhook
3. Image built and scanned
4. Image pushed to ECR
5. Manifest updated
6. ArgoCD detects change
7. EKS updates running pods automatically

---

## DevOps Concepts Demonstrated

- Infrastructure as Code (IaC)
- Configuration as Code
- CI/CD automation
- GitOps workflow
- Container security
- Kubernetes deployment
- AWS cloud architecture

---

## Author

Anas Tarek
Cloud and DevOps Engineer
