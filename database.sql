-- Database creation
-- Cần được chạy tách biệt nếu chưa có database
-- CREATE DATABASE personal_notebook_db;

-- Connect tới database personal_notebook_db trước khi chạy các lệnh dưới!

-- 1. Bảng users
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

-- 2. Bảng roles
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- 3. Bảng trung gian users_roles (Many-To-Many)
CREATE TABLE users_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

-- 4. Bảng notebooks
CREATE TABLE notebooks (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    category VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_notebook_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Insert Roles cơ bản
INSERT INTO roles (name) VALUES ('ROLE_USER'), ('ROLE_ADMIN') ON CONFLICT DO NOTHING;

-- Ghi chú: Hibernate (Spring Data JPA) đã được cấu hình 'update' nên bạn
-- có thể không cần thiết phải chạy bằng tay file SQL này nếu chạy app luôn.
-- Hệ thống cũng sẽ tự động tạo tài khoản admin/admin123 trên lần chạy đầu tiên.
