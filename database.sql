-- ============================================================
-- Database: personal_notebook_db (MySQL 8+)
-- ============================================================
-- Tạo database nếu chưa có (chạy với user có quyền):
CREATE DATABASE IF NOT EXISTS personal_notebook_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE personal_notebook_db;

-- 1. Bảng users
CREATE TABLE IF NOT EXISTS users (
    id       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Bảng roles
CREATE TABLE IF NOT EXISTS roles (
    id   BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Bảng trung gian users_roles (Many-To-Many)
CREATE TABLE IF NOT EXISTS users_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Bảng notebooks
CREATE TABLE IF NOT EXISTS notebooks (
    id         BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title      VARCHAR(255) NOT NULL,
    content    TEXT         NOT NULL,
    category   VARCHAR(255) NOT NULL,
    created_at DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_id    BIGINT       NOT NULL,
    CONSTRAINT fk_notebook_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Insert Roles cơ bản (bỏ qua nếu đã tồn tại)
INSERT IGNORE INTO roles (name) VALUES ('ROLE_USER'), ('ROLE_ADMIN');

-- Ghi chú: Hibernate (Spring Data JPA) đã được cấu hình 'update' nên bạn
-- có thể không cần phải chạy tay file SQL này nếu chạy app luôn.
-- Hệ thống cũng sẽ tự động tạo tài khoản admin/admin123 trên lần chạy đầu tiên.
