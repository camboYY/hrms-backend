-- Create enum type for attendance status
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'attendance_status') THEN
        CREATE TYPE attendance_status AS ENUM (
            'PRESENT',
            'ON_LEAVE',
            'ABSENT',
            'LATE',
            'HALF_DAY'
        );
    END IF;
END $$;

-- Create attendances table
CREATE TABLE IF NOT EXISTS attendances (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    check_in_time TIMESTAMP,
    check_out_time TIMESTAMP,
    date DATE NOT NULL,
    note VARCHAR(255),
    location VARCHAR(255),
    status attendance_status NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_employee_date UNIQUE (employee_id, date)
);

-- Index for faster lookup
CREATE INDEX IF NOT EXISTS idx_attendance_employee_date
ON attendances (employee_id, date);


