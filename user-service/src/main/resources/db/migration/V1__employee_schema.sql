-- PostgreSQL schema

CREATE TYPE employee_status_enum AS ENUM ('ACTIVE','RESIGNED','TERMINATED');

CREATE TABLE departments (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL UNIQUE,
  description VARCHAR(500),
  parent_department_id BIGINT REFERENCES departments(id) ON DELETE SET NULL
);

CREATE TABLE positions (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(150) NOT NULL,
  description VARCHAR(500),
  department_id BIGINT NOT NULL REFERENCES departments(id) ON DELETE CASCADE
);

CREATE TABLE employees (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  employee_code VARCHAR(50) NOT NULL UNIQUE,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  gender VARCHAR(20),
  dob DATE,
  marital_status VARCHAR(50),
  hire_date DATE,
  job_title VARCHAR(150),
  department_id BIGINT REFERENCES departments(id) ON DELETE SET NULL,
  position_id BIGINT REFERENCES positions(id) ON DELETE SET NULL,
  manager_id BIGINT REFERENCES employees(id) ON DELETE SET NULL,
  status employee_status_enum NOT NULL DEFAULT 'ACTIVE',
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE employee_contacts (
  id BIGSERIAL PRIMARY KEY,
  employee_id BIGINT NOT NULL REFERENCES employees(id) ON DELETE CASCADE,
  phone VARCHAR(50),
  email VARCHAR(255),
  address TEXT,
  emergency_contact VARCHAR(255)
);

CREATE TABLE employee_documents (
  id BIGSERIAL PRIMARY KEY,
  employee_id BIGINT NOT NULL REFERENCES employees(id) ON DELETE CASCADE,
  doc_type VARCHAR(100) NOT NULL,
  file_url TEXT NOT NULL,
  issued_date DATE,
  expiry_date DATE
);

-- trigger to update employees.updated_at
CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_employees_updated_at
BEFORE UPDATE ON employees
FOR EACH ROW
EXECUTE FUNCTION set_updated_at();
