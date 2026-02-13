variable "project_name" { type = string }

variable "cluster_name" { type = string }
variable "subnet_ids" { type = list(string) }

variable "node_instance_types" {
type    = list(string)
default = ["t3.small"]
}

variable "desired_size" { 
    type = number 
    default = 2 
    }

variable "min_size"     { 
type = number 
default = 1 
}
variable "max_size"     { 
type = number 
default = 2
}
