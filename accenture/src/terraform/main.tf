terraform {
  required_version = ">= 1.6.0"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.40"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

resource "aws_vpc" "franchise" {
  cidr_block = "10.0.0.0/16"
  tags = { Name = "franchise-vpc" }
}

resource "aws_subnet" "public" {
  vpc_id                  = aws_vpc.franchise.id
  cidr_block              = "10.0.1.0/24"
  map_public_ip_on_launch = true
  availability_zone       = "${var.aws_region}a"
}

resource "aws_internet_gateway" "gw" {
  vpc_id = aws_vpc.franchise.id
}

resource "aws_route_table" "public" {
  vpc_id = aws_vpc.franchise.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.gw.id
  }
}

resource "aws_route_table_association" "public" {
  subnet_id      = aws_subnet.public.id
  route_table_id = aws_route_table.public.id
}

resource "aws_security_group" "app_sg" {
  name        = "franchise-app-sg"
  description = "Permite tráfico HTTP y MySQL"
  vpc_id      = aws_vpc.franchise.id

  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    cidr_blocks = ["10.0.0.0/16"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_db_subnet_group" "mysql" {
  name       = "franchise-db-subnet"
  subnet_ids = [aws_subnet.public.id]
}

resource "aws_db_instance" "mysql" {
  identifier              = "franchise-db"
  engine                  = "mysql"
  engine_version          = "8.0"
  instance_class          = "db.t3.micro"
  allocated_storage       = 20
  username                = var.db_username
  password                = var.db_password
  vpc_security_group_ids  = [aws_security_group.app_sg.id]
  db_subnet_group_name    = aws_db_subnet_group.mysql.name
  publicly_accessible     = true
  skip_final_snapshot     = true
}

resource "aws_ecs_cluster" "cluster" {
  name = "franchise-cluster"
}

resource "aws_ecs_task_definition" "task" {
  family                   = "franchise-task"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "512"
  memory                   = "1024"

  container_definitions = jsonencode([
    {
      name  = "accenture"
      image = var.app_image
      portMappings = [{ containerPort = 8080, hostPort = 8080 }]
      environment = [
        { name = "SPRING_DATASOURCE_URL", value ="jdbc:mysql://${aws_db_instance.mysql.address}:3306/franchise_db?useSSL=NO&allowPublicKeyRetrieval=true" },
        { name = "SPRING_DATASOURCE_USERNAME", value = var.db_username },
        { name = "SPRING_DATASOURCE_PASSWORD", value = var.db_password }
      ]
    }
  ])
}

resource "aws_ecs_service" "service" {
  name            = "franchise-service"
  cluster         = aws_ecs_cluster.cluster.id
  task_definition = aws_ecs_task_definition.task.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets         = [aws_subnet.public.id]
    security_groups = [aws_security_group.app_sg.id]
    assign_public_ip = true
  }
}