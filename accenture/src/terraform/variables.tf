variable "aws_region" {
  description = "Región AWS"
  type        = string
  default     = "us-east-1"
}

variable "db_username" {
  type      = string
  default   = "franchise_user"
}

variable "db_password" {
  type      = string
  sensitive = true
}

variable "app_image" {
  description = "Imagen docker publicada en ECR"
  type        = string
}