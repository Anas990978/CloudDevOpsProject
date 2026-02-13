terraform {
  backend "s3" {
    bucket         = "tf-lockstate-bucket-123456789"
    key            = "infra/terraform.tfstate"
    region         = "us-east-1"
    dynamodb_table = "terraform-locks"
    encrypt        = false
  }
}
