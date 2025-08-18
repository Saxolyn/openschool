--liquibase formatted sql

--changeset notification:003
--comment: Insert default email templates

-- Welcome email template
INSERT INTO email_templates (
    id, 
    name, 
    display_name, 
    description, 
    subject, 
    html_content, 
    text_content, 
    available_variables, 
    active, 
    category, 
    language, 
    version, 
    created_at, 
    updated_at
) VALUES (
    'a1b2c3d4-e5f6-7890-abcd-ef1234567890',
    'welcome-user',
    'Welcome New User',
    'Welcome email template for new users',
    'Welcome to {{schoolName}}!',
    '<html><body><h1>Welcome to {{schoolName}}, {{userName}}!</h1><p>We are excited to have you join our school community.</p><p>Your account has been created successfully. You can now log in to access the system.</p><p>If you have any questions, please contact our support team.</p><p>Best regards,<br>{{schoolName}} Team</p></body></html>',
    'Welcome to {{schoolName}}, {{userName}}!

We are excited to have you join our school community.

Your account has been created successfully. You can now log in to access the system.

If you have any questions, please contact our support team.

Best regards,
{{schoolName}} Team',
    '["userName", "schoolName", "loginUrl"]',
    true,
    'USER_MANAGEMENT',
    'en',
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Password reset template
INSERT INTO email_templates (
    id, 
    name, 
    display_name, 
    description, 
    subject, 
    html_content, 
    text_content, 
    available_variables, 
    active, 
    category, 
    language, 
    version, 
    created_at, 
    updated_at
) VALUES (
    'b2c3d4e5-f6g7-8901-bcde-f23456789012',
    'password-reset',
    'Password Reset',
    'Password reset email template',
    'Reset Your Password - {{schoolName}}',
    '<html><body><h1>Password Reset Request</h1><p>Hello {{userName}},</p><p>We received a request to reset your password for your {{schoolName}} account.</p><p>Click the link below to reset your password:</p><p><a href="{{resetUrl}}">Reset Password</a></p><p>This link will expire in {{expirationTime}} minutes.</p><p>If you did not request this password reset, please ignore this email.</p><p>Best regards,<br>{{schoolName}} Team</p></body></html>',
    'Password Reset Request

Hello {{userName}},

We received a request to reset your password for your {{schoolName}} account.

Click the link below to reset your password:
{{resetUrl}}

This link will expire in {{expirationTime}} minutes.

If you did not request this password reset, please ignore this email.

Best regards,
{{schoolName}} Team',
    '["userName", "schoolName", "resetUrl", "expirationTime"]',
    true,
    'SECURITY',
    'en',
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Grade notification template
INSERT INTO email_templates (
    id, 
    name, 
    display_name, 
    description, 
    subject, 
    html_content, 
    text_content, 
    available_variables, 
    active, 
    category, 
    language, 
    version, 
    created_at, 
    updated_at
) VALUES (
    'c3d4e5f6-g7h8-9012-cdef-345678901234',
    'grade-notification',
    'Grade Notification',
    'Template for grade-related notifications',
    'Grade Update - {{studentName}}',
    '<html><body><h1>Grade Update Notification</h1><p>Dear {{parentName}},</p><p>This is to inform you that a new grade has been recorded for {{studentName}} in {{subject}}.</p><p><strong>Grade:</strong> {{grade}}</p><p><strong>Subject:</strong> {{subject}}</p><p><strong>Teacher:</strong> {{teacherName}}</p><p><strong>Date:</strong> {{gradeDate}}</p><p>You can view more details by logging into the parent portal.</p><p>Best regards,<br>{{schoolName}} Academic Team</p></body></html>',
    'Grade Update Notification

Dear {{parentName}},

This is to inform you that a new grade has been recorded for {{studentName}} in {{subject}}.

Grade: {{grade}}
Subject: {{subject}}
Teacher: {{teacherName}}
Date: {{gradeDate}}

You can view more details by logging into the parent portal.

Best regards,
{{schoolName}} Academic Team',
    '["parentName", "studentName", "subject", "grade", "teacherName", "gradeDate", "schoolName"]',
    true,
    'ACADEMIC',
    'en',
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Attendance notification template
INSERT INTO email_templates (
    id, 
    name, 
    display_name, 
    description, 
    subject, 
    html_content, 
    text_content, 
    available_variables, 
    active, 
    category, 
    language, 
    version, 
    created_at, 
    updated_at
) VALUES (
    'd4e5f6g7-h8i9-0123-defg-456789012345',
    'attendance-notification',
    'Attendance Notification',
    'Template for attendance-related notifications',
    'Attendance Alert - {{studentName}}',
    '<html><body><h1>Attendance Notification</h1><p>Dear {{parentName}},</p><p>This is to inform you about the attendance status of {{studentName}}.</p><p><strong>Date:</strong> {{attendanceDate}}</p><p><strong>Status:</strong> {{attendanceStatus}}</p><p><strong>Class:</strong> {{className}}</p><p>{{#if reason}}<strong>Reason:</strong> {{reason}}{{/if}}</p><p>If you have any questions, please contact the school office.</p><p>Best regards,<br>{{schoolName}} Administration</p></body></html>',
    'Attendance Notification

Dear {{parentName}},

This is to inform you about the attendance status of {{studentName}}.

Date: {{attendanceDate}}
Status: {{attendanceStatus}}
Class: {{className}}
{{#if reason}}Reason: {{reason}}{{/if}}

If you have any questions, please contact the school office.

Best regards,
{{schoolName}} Administration',
    '["parentName", "studentName", "attendanceDate", "attendanceStatus", "className", "reason", "schoolName"]',
    true,
    'ACADEMIC',
    'en',
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- System maintenance template
INSERT INTO email_templates (
    id, 
    name, 
    display_name, 
    description, 
    subject, 
    html_content, 
    text_content, 
    available_variables, 
    active, 
    category, 
    language, 
    version, 
    created_at, 
    updated_at
) VALUES (
    'e5f6g7h8-i9j0-1234-efgh-567890123456',
    'system-maintenance',
    'System Maintenance',
    'Template for system maintenance notifications',
    'Scheduled Maintenance - {{schoolName}} System',
    '<html><body><h1>Scheduled System Maintenance</h1><p>Dear {{userName}},</p><p>We will be performing scheduled maintenance on the {{schoolName}} system.</p><p><strong>Maintenance Window:</strong> {{maintenanceStart}} to {{maintenanceEnd}}</p><p><strong>Expected Duration:</strong> {{duration}}</p><p>During this time, the system will be unavailable. We apologize for any inconvenience.</p><p>{{#if alternativeAccess}}<strong>Alternative Access:</strong> {{alternativeAccess}}{{/if}}</p><p>Thank you for your patience.</p><p>Best regards,<br>{{schoolName}} IT Team</p></body></html>',
    'Scheduled System Maintenance

Dear {{userName}},

We will be performing scheduled maintenance on the {{schoolName}} system.

Maintenance Window: {{maintenanceStart}} to {{maintenanceEnd}}
Expected Duration: {{duration}}

During this time, the system will be unavailable. We apologize for any inconvenience.

{{#if alternativeAccess}}Alternative Access: {{alternativeAccess}}{{/if}}

Thank you for your patience.

Best regards,
{{schoolName}} IT Team',
    '["userName", "schoolName", "maintenanceStart", "maintenanceEnd", "duration", "alternativeAccess"]',
    true,
    'SYSTEM',
    'en',
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);
