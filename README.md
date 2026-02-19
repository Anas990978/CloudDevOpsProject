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

---

## Project Structure

.
├── App/
│   ├── FinalProject/
│   │   ├── app.py
│   │   ├── requirements.txt
│   │   └── templates/
│   └── Dockerfile
│
├── K8s/
│   ├── ns.yaml
│   ├── deployment.yaml
│   └── service.yaml
│
├── Jenkins/
│   └── jenkinsfile
│
├── Ansible/
│   ├── roles/
│   │   ├── common-tools/
│   │   ├── docker/
│   │   ├── jenkins/
│   │   └── trivy/
│   └── playbook.yaml
│
├── Terraform/
│   ├── modules/
│   │   ├── network/
│   │   ├── jenkins/
│   │   ├── eks/
│   │   └── ecr/
│   ├── Backend/
│   └── infra/
│
└── README.md

---

## Docker (Build and Run)

Build locally:

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

Jenkins configuration:
- Shared library name: `shared-lib`
- AWS credentials ID: `aws-creds`
- GitHub token ID: `github-token`
- ECR URI: `637423620989.dkr.ecr.us-east-1.amazonaws.com/ivolve-app`
- Image tag: `${BUILD_NUMBER}`

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

Login command:

<<<<<<< HEAD
1. Terraform provisions AWS infra: VPC, subnets, IGW/NACL, EC2 Jenkins, EKS, ECR, CloudWatch.
2. Ansible configures EC2 instances (tools, Docker, Jenkins, Trivy).
3. Jenkins CI builds/scans/pushes the Docker image and updates K8s manifests.
4. Kubernetes runs the application in the `iVolve` namespace.
5. ArgoCD syncs and deploys changes from Git to the cluster.
<img width="6805" height="3476" alt="ivolve-proj2" src="https://github.com/user-attachments/assets/a6385570-d7ab-4d39-bd1b-af8638db9e09" />
=======
```bash
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 637423620989.dkr.ecr.us-east-1.amazonaws.com
```
>>>>>>> 77b0d2d (Documentation)

---

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
