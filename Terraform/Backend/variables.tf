variable "state_bucket_name" {
  type = string
  default = "tf-lockstate-bucket-123456789"
}

variable "lock_table_name" {
  type    = string
  default = "terraform-locks"
}
