-- MySQL 初始化脚本：学生信息管理系统
-- 包含数据库 studentdb 以及 users、student 两张表

-- 创建数据库（如不存在）
CREATE DATABASE IF NOT EXISTS studentdb
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

USE studentdb;

-- 用户表（对应 User 实体）
CREATE TABLE IF NOT EXISTS users (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  username VARCHAR(64) NOT NULL,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(32) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_users_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 学生表（对应 Student 实体，与 users 一对一）
CREATE TABLE IF NOT EXISTS student (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  age INT,
  major VARCHAR(100),
  user_id BIGINT UNSIGNED,
  PRIMARY KEY (id),
  UNIQUE KEY uk_student_user_id (user_id),
  CONSTRAINT fk_student_user
    FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE CASCADE
    ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
