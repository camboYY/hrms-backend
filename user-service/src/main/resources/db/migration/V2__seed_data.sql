-- Departments
INSERT INTO departments (name, description) VALUES
('Head Office','Main corporate HQ'),
('IT','Information Technology'),
('HR','Human Resources');

-- Sub-department
INSERT INTO departments (name, description, parent_department_id)
VALUES ('Security', 'Security under IT', (SELECT id FROM departments WHERE name='IT'));

-- Positions
INSERT INTO positions (name, description, department_id) VALUES
('Software Engineer','Builds software', (SELECT id FROM departments WHERE name='IT')),
('HR Officer','Handles HR ops', (SELECT id FROM departments WHERE name='HR')),
('IT Security Analyst','Security role', (SELECT id FROM departments WHERE name='Security'));

-- Employees
INSERT INTO employees
(user_id, employee_code, first_name, last_name, gender, dob, marital_status, hire_date, job_title,
 department_id, position_id, manager_id, status)
VALUES
(1001,'EMP-0001','Sophea','Chan','Female','1993-04-12','Single','2021-01-15','Software Engineer',
 (SELECT id FROM departments WHERE name='IT'),
 (SELECT id FROM positions WHERE name='Software Engineer'),
 NULL,'ACTIVE'),

(1002,'EMP-0002','Vuthy','Kim','Male','1988-08-05','Married','2020-03-01','HR Officer',
 (SELECT id FROM departments WHERE name='HR'),
 (SELECT id FROM positions WHERE name='HR Officer'),
 NULL,'ACTIVE'),

(1003,'EMP-0003','Dara','Sok','Male','1990-11-20','Married','2019-07-10','IT Security Analyst',
 (SELECT id FROM departments WHERE name='Security'),
 (SELECT id FROM positions WHERE name='IT Security Analyst'),
 (SELECT id FROM employees WHERE employee_code='EMP-0001'),'ACTIVE');

-- Contacts
INSERT INTO employee_contacts (employee_id, phone, email, address, emergency_contact)
SELECT id, '+85512345678', 'sophea.chan@company.com', 'Phnom Penh', 'Mother: +85511111111'
FROM employees WHERE employee_code='EMP-0001';

INSERT INTO employee_contacts (employee_id, phone, email, address, emergency_contact)
SELECT id, '+85598765432', 'vuthy.kim@company.com', 'Phnom Penh', 'Wife: +85522222222'
FROM employees WHERE employee_code='EMP-0002';

-- Documents
INSERT INTO employee_documents (employee_id, doc_type, file_url, issued_date, expiry_date)
SELECT id, 'ID Card', 'https://files.company.com/docs/EMP-0001-id.pdf', '2019-01-01', '2029-01-01'
FROM employees WHERE employee_code='EMP-0001';

INSERT INTO employee_documents (employee_id, doc_type, file_url, issued_date, expiry_date)
SELECT id, 'Employment Contract', 'https://files.company.com/docs/EMP-0002-contract.pdf', '2020-03-01', NULL
FROM employees WHERE employee_code='EMP-0002';
