CREATE TABLE salary_structure (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    basic_salary NUMERIC(10,2) NOT NULL CHECK (basic_salary >= 0),
    tax_rate NUMERIC(5,4) DEFAULT 0.05 CHECK (tax_rate BETWEEN 0 AND 1),
    nssf_rate NUMERIC(5,4) DEFAULT 0.02 CHECK (nssf_rate BETWEEN 0 AND 1),
    effective_from DATE NOT NULL,
    UNIQUE (employee_id, effective_from)
);


CREATE TABLE payroll (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    payroll_month DATE NOT NULL, -- store as first day of month
    gross_salary NUMERIC(12,2) NOT NULL CHECK (gross_salary >= 0),
    tax NUMERIC(12,2) NOT NULL CHECK (tax >= 0),
    nssf NUMERIC(12,2) NOT NULL CHECK (nssf >= 0),
    net_salary NUMERIC(12,2) NOT NULL CHECK (net_salary >= 0),
    status VARCHAR(20) DEFAULT 'GENERATED'
        CHECK (status IN ('GENERATED', 'APPROVED', 'PAID')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (employee_id, payroll_month)
);


CREATE TABLE payroll_item (
    id BIGSERIAL PRIMARY KEY,
    payroll_id BIGINT NOT NULL REFERENCES payroll(id) ON DELETE CASCADE,
    type VARCHAR(20) NOT NULL
        CHECK (type IN ('ALLOWANCE', 'DEDUCTION')),
    name VARCHAR(100) NOT NULL,
    amount NUMERIC(12,2) NOT NULL CHECK (amount >= 0)
);

INSERT INTO salary_structure
(employee_id, basic_salary, tax_rate, nssf_rate, effective_from)
VALUES
(7, 800.00, 0.05, 0.02, '2025-01-01');

INSERT INTO payroll
(employee_id, payroll_month, gross_salary, tax, nssf, net_salary, status)
VALUES
(7, '2025-12-01', 1030.00, 51.50, 20.60, 957.90, 'PAID'),
(8, '2025-12-01', 920.00, 46.00, 18.40, 855.60, 'PAID'),
(9, '2025-12-01', 1120.00, 56.00, 22.40, 1041.60, 'PAID'),
(10,'2025-11-01', 1000.00, 50.00, 20.00, 930.00, 'PAID');

-- Payroll ID = 1
INSERT INTO payroll_item (payroll_id, type, name, amount) VALUES
(1, 'ALLOWANCE', 'Housing Allowance', 200.00),
(1, 'ALLOWANCE', 'Transport Allowance', 50.00),
(1, 'ALLOWANCE', 'Meal Allowance', 30.00),
(1, 'DEDUCTION', 'Tax', 51.50),
(1, 'DEDUCTION', 'NSSF', 20.60);

-- Payroll ID = 2
INSERT INTO payroll_item (payroll_id, type, name, amount) VALUES
(2, 'ALLOWANCE', 'Housing Allowance', 180.00),
(2, 'ALLOWANCE', 'Transport Allowance', 40.00),
(2, 'DEDUCTION', 'Tax', 46.00),
(2, 'DEDUCTION', 'Leave Deduction', 18.40);

-- Payroll ID = 3
INSERT INTO payroll_item (payroll_id, type, name, amount) VALUES
(3, 'ALLOWANCE', 'Overtime', 120.00),
(3, 'ALLOWANCE', 'Performance Bonus', 300.00),
(3, 'DEDUCTION', 'Tax', 56.00),
(3, 'DEDUCTION', 'NSSF', 22.40);

-- Payroll ID = 4
INSERT INTO payroll_item (payroll_id, type, name, amount) VALUES
(4, 'ALLOWANCE', 'Housing Allowance', 220.00),
(4, 'ALLOWANCE', 'Transport Allowance', 60.00),
(4, 'DEDUCTION', 'Tax', 50.00),
(4, 'DEDUCTION', 'Absence Deduction', 20.00);

SELECT
    payroll_id,
    SUM(CASE WHEN type = 'ALLOWANCE' THEN amount ELSE 0 END) AS total_allowances,
    SUM(CASE WHEN type = 'DEDUCTION' THEN amount ELSE 0 END) AS total_deductions
FROM payroll_item
GROUP BY payroll_id
ORDER BY payroll_id;

