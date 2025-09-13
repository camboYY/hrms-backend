CREATE TYPE leave_status_enum AS ENUM ('PENDING','APPROVED','REJECTED','CANCELLED');

CREATE TABLE leave_types (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL UNIQUE,
  max_days_per_year INT NOT NULL,
  carry_forward_allowed BOOLEAN NOT NULL DEFAULT FALSE,
  requires_approval BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE leave_requests (
  id BIGSERIAL PRIMARY KEY,
  employee_id BIGINT NOT NULL, -- FK to employees.id
  leave_type_id BIGINT NOT NULL REFERENCES leave_types(id) ON DELETE CASCADE,
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  reason TEXT,
  status leave_status_enum NOT NULL DEFAULT 'PENDING',
  approver_id BIGINT, -- FK to employees.id
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE leave_balances (
  id BIGSERIAL PRIMARY KEY,
  employee_id BIGINT NOT NULL,
  leave_type_id BIGINT NOT NULL REFERENCES leave_types(id) ON DELETE CASCADE,
  allocated_days INT NOT NULL,
  used_days INT NOT NULL,
  remaining_days INT NOT NULL
);

-- Trigger for updated_at
CREATE OR REPLACE FUNCTION set_updated_at_leave()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_leave_requests_updated_at
BEFORE UPDATE ON leave_requests
FOR EACH ROW
EXECUTE FUNCTION set_updated_at_leave();

INSERT INTO leave_types (name, max_days_per_year) VALUES
('Annual', 20),
('Sick', 10),
('Maternity', 90);

INSERT INTO leave_balances (employee_id, leave_type_id, allocated_days, used_days, remaining_days) VALUES
(1, 1, 20, 5, 15),
(1, 2, 10, 2, 8),
(2, 1, 20, 0, 20);
