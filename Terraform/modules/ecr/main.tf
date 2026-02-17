resource "aws_ecr_repository" "this" {
  name = var.repo_name
  force_delete = true
  image_scanning_configuration {
    scan_on_push = true
  }

  tags = {
    Name = "${var.project_name}-ecr"
  }
}
