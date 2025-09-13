CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('ACTIVE','INACTIVE')),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE permissions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL UNIQUE
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE role_permissions (
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    permission_id BIGINT NOT NULL REFERENCES permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

-- Seed roles
INSERT INTO roles (name) VALUES ('ROLE_ADMIN'), ('ROLE_EMPLOYEE'), ('ROLE_MANAGER'), ('ROLE_HR');

-- Seed Permissions
INSERT INTO permissions (name) VALUES
('CREATE_USER'),
('VIEW_USER'),
('UPDATE_USER'),
('DELETE_USER'),
('ASSIGN_ROLE'),
('CREATE_LEAVE'),
('APPROVE_LEAVE'),
('VIEW_PAYROLL');


-- Assign Admin role all permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p WHERE r.name = 'ROLE_ADMIN';

-- HR can create/view/update users, view payroll, approve leave
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name IN ('CREATE_USER','VIEW_USER','UPDATE_USER','VIEW_PAYROLL','APPROVE_LEAVE')
WHERE r.name = 'ROLE_HR';

-- MANAGER can view users, approve leave
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name IN ('VIEW_USER','APPROVE_LEAVE')
WHERE r.name = 'ROLE_MANAGER';

-- EMPLOYEE can create leave requests and view themselves
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name IN ('CREATE_LEAVE','VIEW_USER')
WHERE r.name = 'ROLE_EMPLOYEE';

-- ===========================================
-- CREATE DEFAULT ADMIN USER
-- ===========================================
-- admin user with BCrypt password (admin123)
INSERT INTO users (username, email, password, status)
VALUES ('admin', 'admin@hrms.com', '$2a$10$.2MCjJvPPZqHnRCIwdgnQelaUJZzeh4OcJDEYc6cSAQiRQaygMYmK', 'ACTIVE');

-- assign role (note: no need to prefix with ROLE_ here, JwtUtil + AuthConfig will handle)
INSERT INTO user_roles (user_id, role_id)
VALUES ((SELECT id FROM users WHERE username = 'admin'), (SELECT id FROM roles WHERE name = 'ROLE_ADMIN'));

