output "rds_endpoint" {
  description = "Endpoint de MySQL"
  value       = aws_db_instance.mysql.address
}

output "ecs_cluster" {
  value = aws_ecs_cluster.cluster.name
}