variable "project_name" { type = string }
variable "vpc_id" { type = string }
variable "subnet_id" { type = string }

variable "allowed_cidrs" { type = list(string) }
variable "instance_type" { type = string }
variable "key_name" { type = string }
