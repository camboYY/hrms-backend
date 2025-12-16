
CREATE TABLE notification_templates (
    id SERIAL PRIMARY KEY,
    code VARCHAR(100) UNIQUE NOT NULL,
    title VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    channel VARCHAR(50) NOT NULL,  -- email, sms, push, in_app
    created_at TIMESTAMP DEFAULT NOW()
);

INSERT INTO notification_templates (code, title, body, channel) VALUES
('LEAVE_APPROVED', 'Leave Approved', 'Your leave request has been approved.', 'push'),
('LEAVE_REJECTED', 'Leave Rejected', 'Your leave request has been rejected.', 'push'),
('PAYROLL_GENERATED', 'Payslip Available', 'Your payslip is now ready.', 'email'),
('ATTENDANCE_LATE', 'Late Check-in', 'You checked in late today.', 'push'),
('BIRTHDAY_GREETINGS', 'Happy Birthday!', 'Wishing you an amazing birthday 🎉', 'in_app');


CREATE TABLE notifications (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,                   -- User receiving the notification
    template_code VARCHAR(50) NOT NULL,       -- Links to notification_templates.code
    type VARCHAR(20),                          -- Optional override: push | email | in_app
    title VARCHAR(255),                        -- Can use template.title
    body TEXT,                                 -- Can use template.body
    status VARCHAR(20) DEFAULT 'PENDING',      -- PENDING | SENT | FAILED
    created_at TIMESTAMP DEFAULT now(),
    sent_at TIMESTAMP                          -- When notification was sent
);

-- Optional: Example mock data
INSERT INTO notifications (user_id, template_code, type, title, body, status) VALUES
(101, 'LEAVE_APPROVED', 'push', 'Leave Approved', 'Your leave request has been approved.', 'SENT'),
(102, 'LEAVE_REJECTED', 'push', 'Leave Rejected', 'Your leave request has been rejected.', 'PENDING'),
(103, 'PAYROLL_GENERATED', 'email', 'Payslip Available', 'Your payslip is now ready.', 'SENT'),
(104, 'ATTENDANCE_LATE', 'push', 'Late Check-in', 'You checked in late today.', 'FAILED'),
(105, 'BIRTHDAY_GREETINGS', 'in_app', 'Happy Birthday!', 'Wishing you an amazing birthday 🎉', 'SENT');


CREATE TABLE user_notification_preferences (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,  -- from User Service
    email_enabled BOOLEAN DEFAULT TRUE,
    sms_enabled BOOLEAN DEFAULT FALSE,
    push_enabled BOOLEAN DEFAULT TRUE,
    in_app_enabled BOOLEAN DEFAULT TRUE,
    updated_at TIMESTAMP DEFAULT NOW()
);


INSERT INTO user_notification_preferences
(user_id, email_enabled, sms_enabled, push_enabled, in_app_enabled) VALUES
(101, TRUE, FALSE, TRUE, TRUE),
(102, TRUE, TRUE, TRUE, TRUE),
(103, TRUE, TRUE, FALSE, TRUE),
(104, FALSE, TRUE, TRUE, TRUE);

CREATE TABLE notification_logs (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    template_id INT REFERENCES notification_templates(id),
    title VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    channel VARCHAR(50) NOT NULL,
    status VARCHAR(50) DEFAULT 'SENT',   -- SENT, FAILED, DELIVERED, READ
    metadata JSONB,                     -- firebase msg_id, email_id, sms provider ID, etc.
    sent_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_notification_logs_user ON notification_logs(user_id);
CREATE INDEX idx_notification_logs_status ON notification_logs(status);

INSERT INTO notification_logs (user_id, template_id, title, body, channel, status, metadata) VALUES
(101, 1, 'Leave Approved', 'Your leave request has been approved.', 'push', 'DELIVERED', '{"msg_id":"fc123"}'),
(102, 3, 'Payslip Available', 'Payslip for Nov 2025 is ready.', 'email', 'SENT', '{"provider":"SendGrid"}'),
(103, 4, 'Late Check-in', 'You checked in late at 09:21 AM.', 'push', 'READ', '{"device":"iPhone"}'),
(101, 5, 'Happy Birthday!', 'Enjoy your special day 🎉', 'in_app', 'DELIVERED', '{"app":"HRMS"}');


CREATE TABLE scheduled_notifications (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    template_id INT REFERENCES notification_templates(id),
    payload JSONB,
    trigger_time TIMESTAMP NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING',  -- PENDING, SENT, CANCELLED
    created_at TIMESTAMP DEFAULT NOW()
);


CREATE INDEX idx_scheduled_trigger_time
    ON scheduled_notifications(trigger_time);

INSERT INTO scheduled_notifications (user_id, template_id, payload, trigger_time) VALUES
(101, 3, '{"month":"December","year":2025}', '2025-12-31 18:00:00'),
(102, 5, '{"event":"birthday"}', '2025-03-14 09:00:00'),
(104, 4, '{"expected_checkin":"08:00"}', '2025-12-06 08:30:00');




