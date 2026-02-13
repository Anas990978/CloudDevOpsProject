locals {
  project_name = "ivolve-proj"
  vpc_cidr     = "10.0.0.0/16"

  public_subnets  = ["10.0.1.0/24", "10.0.2.0/24"]

  private_subnets = ["10.0.3.0/24", "10.0.4.0/24"]

  azs = ["us-east-1a", "us-east-1b"]

  allowed_cidrs = ["102.190.51.30/32"]
  key_name      = "Ivolve-proj"
  ecr_repo_name = "ivolve-app"

  eks_cluster_name = "ivolve-proj-eks"
}

module "network" {
  source               = "../modules/network"
  project_name         = local.project_name
  vpc_cidr             = local.vpc_cidr
  public_subnet_cidrs  = local.public_subnets
  private_subnet_cidrs = local.private_subnets
  azs                  = local.azs
  eks_cluster_name     = local.eks_cluster_name
}

module "jenkins" {
  source       = "../modules/jenkins"
  project_name = local.project_name
  vpc_id       = module.network.vpc_id
  subnet_id    = module.network.public_subnet_ids[0]

  allowed_cidrs = local.allowed_cidrs
  instance_type = "t3.small"
  key_name      = local.key_name
}

module "ecr" {
  source       = "../modules/ecr"
  project_name = local.project_name
  repo_name    = local.ecr_repo_name
}

module "eks" {
  source       = "../modules/eks"
  project_name = local.project_name

  cluster_name = local.eks_cluster_name
  subnet_ids   = module.network.private_subnet_ids

  node_instance_types = ["t3.small"]
  desired_size        = 2
  min_size            = 1
  max_size            = 2
}
