-- USERS
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

-- ROLES
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- USERS_ROLES
CREATE TABLE users_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- NOTEBOOKS (KHÔNG còn category string nữa)
CREATE TABLE notebooks (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_notebook_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- CATEGORIES
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    color VARCHAR(20) DEFAULT '#6366f1'
);

-- MANY TO MANY
CREATE TABLE notebook_categories (
    notebook_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (notebook_id, category_id),
    CONSTRAINT fk_nc_notebook FOREIGN KEY (notebook_id) REFERENCES notebooks(id) ON DELETE CASCADE,
    CONSTRAINT fk_nc_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

-- SEED DATA
INSERT INTO roles (name) VALUES ('ROLE_USER'), ('ROLE_ADMIN')
ON CONFLICT DO NOTHING;

INSERT INTO categories (name, color) VALUES
    ('Cá nhân',  '#22c55e'),
    ('Công việc','#3b82f6'),
    ('Học tập',  '#f59e0b'),
    ('Sức khỏe', '#a855f7'),
    ('Khẩn cấp', '#ef4444'),
    ('Khác',     '#64748b')
ON CONFLICT DO NOTHING;


ALTER TABLE notebooks ADD COLUMN color VARCHAR(20);
ALTER TABLE notebooks ADD COLUMN is_pinned BOOLEAN DEFAULT FALSE;
ALTER TABLE notebooks ADD COLUMN is_archived BOOLEAN DEFAULT FALSE;
ALTER TABLE notebooks ADD COLUMN is_deleted BOOLEAN DEFAULT FALSE;