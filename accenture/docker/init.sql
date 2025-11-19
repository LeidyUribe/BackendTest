-- Script de inicialización para MySQL
-- Crea el usuario y otorga permisos

CREATE DATABASE IF NOT EXISTS franchise_db;

CREATE USER IF NOT EXISTS 'franchise_user'@'%' IDENTIFIED BY 'franchise_pass';
GRANT ALL PRIVILEGES ON franchise_db.* TO 'franchise_user'@'%';
FLUSH PRIVILEGES;

