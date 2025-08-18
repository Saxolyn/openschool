--liquibase formatted sql

--changeset notification:001
--comment: Create notification system tables

-- Create notifications table
CREATE TABLE notifications (
    id UUID PRIMARY KEY,
    recipient_id UUID NOT NULL,
    sender_id UUID,
    title VARCHAR(500) NOT NULL,
    content TEXT,
    type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    priority VARCHAR(50) NOT NULL DEFAULT 'NORMAL',
    email VARCHAR(255),
    template_id UUID,
    template_variables JSONB,
    metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sent_at TIMESTAMP,
    delivered_at TIMESTAMP,
    read_at TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    error_message TEXT,
    retry_count INTEGER DEFAULT 0,
    max_retries INTEGER DEFAULT 3,
    retry_at TIMESTAMP
);

-- Create email_templates table
CREATE TABLE email_templates (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    display_name VARCHAR(200),
    description TEXT,
    subject VARCHAR(500) NOT NULL,
    html_content TEXT,
    text_content TEXT,
    available_variables JSONB,
    active BOOLEAN NOT NULL DEFAULT true,
    category VARCHAR(50),
    language VARCHAR(10) DEFAULT 'en',
    version INTEGER NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_by UUID
);

-- Create notification_preferences table
CREATE TABLE notification_preferences (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE,
    email_enabled BOOLEAN NOT NULL DEFAULT true,
    internal_message_enabled BOOLEAN NOT NULL DEFAULT true,
    push_notification_enabled BOOLEAN NOT NULL DEFAULT true,
    sms_enabled BOOLEAN NOT NULL DEFAULT false,
    notification_email VARCHAR(255),
    notification_phone VARCHAR(20),
    preferred_language VARCHAR(10) DEFAULT 'en',
    timezone VARCHAR(50) DEFAULT 'UTC',
    digest_mode BOOLEAN NOT NULL DEFAULT false,
    digest_frequency VARCHAR(20) DEFAULT 'DAILY',
    digest_time VARCHAR(5) DEFAULT '09:00',
    quiet_hours_start VARCHAR(5),
    quiet_hours_end VARCHAR(5),
    category_preferences JSONB,
    custom_preferences JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Add constraints
ALTER TABLE notifications ADD CONSTRAINT chk_notification_type 
    CHECK (type IN ('EMAIL', 'INTERNAL_MESSAGE', 'PUSH_NOTIFICATION', 'SMS'));

ALTER TABLE notifications ADD CONSTRAINT chk_notification_status 
    CHECK (status IN ('PENDING', 'SENDING', 'SENT', 'DELIVERED', 'READ', 'FAILED', 'CANCELLED'));

ALTER TABLE notifications ADD CONSTRAINT chk_notification_priority 
    CHECK (priority IN ('LOW', 'NORMAL', 'HIGH', 'CRITICAL'));

ALTER TABLE email_templates ADD CONSTRAINT chk_template_content 
    CHECK (html_content IS NOT NULL OR text_content IS NOT NULL);

-- Add comments
COMMENT ON TABLE notifications IS 'Stores all notifications sent in the system';
COMMENT ON TABLE email_templates IS 'Stores email templates for notifications';
COMMENT ON TABLE notification_preferences IS 'Stores user notification preferences';

COMMENT ON COLUMN notifications.type IS 'Type of notification: EMAIL, INTERNAL_MESSAGE, PUSH_NOTIFICATION, SMS';
COMMENT ON COLUMN notifications.status IS 'Status of notification: PENDING, SENDING, SENT, DELIVERED, READ, FAILED, CANCELLED';
COMMENT ON COLUMN notifications.priority IS 'Priority level: LOW, NORMAL, HIGH, CRITICAL';
COMMENT ON COLUMN notifications.template_variables IS 'JSON object containing variables for template processing';
COMMENT ON COLUMN notifications.metadata IS 'JSON object containing additional notification metadata';

COMMENT ON COLUMN email_templates.available_variables IS 'JSON array of variable names available in this template';
COMMENT ON COLUMN email_templates.active IS 'Whether this template is active and can be used';
COMMENT ON COLUMN email_templates.version IS 'Version number for tracking template changes';

COMMENT ON COLUMN notification_preferences.category_preferences IS 'JSON object containing category-specific notification preferences';
COMMENT ON COLUMN notification_preferences.custom_preferences IS 'JSON object containing custom user preferences';
