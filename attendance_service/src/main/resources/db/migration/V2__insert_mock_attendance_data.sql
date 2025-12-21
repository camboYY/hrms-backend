INSERT INTO attendances (
    employee_id,
    check_in_time,
    check_out_time,
    date,
    note,
    location,
    status
) VALUES
-- Employee 1
(1, '2025-12-18 08:05:00', '2025-12-18 17:10:00', '2025-12-18', 'On time', 'HQ Office', 'PRESENT'),
(1, NULL, NULL, '2025-12-19', 'Annual leave approved', 'N/A', 'ON_LEAVE'),

-- Employee 2
(2, '2025-12-18 08:45:00', '2025-12-18 17:00:00', '2025-12-18', 'Late arrival', 'Branch Office', 'LATE'),
(2, NULL, NULL, '2025-12-19', 'Sick leave rejected', 'N/A', 'ABSENT'),

-- Employee 3
(3, '2025-12-18 08:00:00', '2025-12-18 12:00:00', '2025-12-18', 'Half day work', 'Remote', 'HALF_DAY');
