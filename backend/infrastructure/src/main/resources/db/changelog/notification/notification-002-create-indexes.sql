--liquibase formatted sql

--changeset notification:002
--comment: Create indexes for notification system tables

-- Indexes for notifications table
CREATE INDEX idx_notification_recipient ON notifications(recipient_id);
CREATE INDEX idx_notification_status ON notifications(status);
CREATE INDEX idx_notification_type ON notifications(type);
CREATE INDEX idx_notification_created ON notifications(created_at);
CREATE INDEX idx_notification_recipient_status ON notifications(recipient_id, status);
CREATE INDEX idx_notification_recipient_type ON notifications(recipient_id, type);
CREATE INDEX idx_notification_recipient_created ON notifications(recipient_id, created_at DESC);
CREATE INDEX idx_notification_sender ON notifications(sender_id);
CREATE INDEX idx_notification_template ON notifications(template_id);
CREATE INDEX idx_notification_retry ON notifications(status, retry_at) WHERE status = 'FAILED';

-- Indexes for email_templates table
CREATE UNIQUE INDEX idx_email_template_name ON email_templates(name);
CREATE INDEX idx_email_template_category ON email_templates(category);
CREATE INDEX idx_email_template_language ON email_templates(language);
CREATE INDEX idx_email_template_active ON email_templates(active);
CREATE INDEX idx_email_template_created ON email_templates(created_at);

-- Indexes for notification_preferences table
CREATE UNIQUE INDEX idx_notification_pref_user ON notification_preferences(user_id);
CREATE INDEX idx_notification_pref_created ON notification_preferences(created_at);

-- Partial indexes for performance optimization
CREATE INDEX idx_notification_unread ON notifications(recipient_id, created_at DESC) 
    WHERE status != 'READ';

CREATE INDEX idx_notification_pending ON notifications(created_at) 
    WHERE status = 'PENDING';

CREATE INDEX idx_notification_failed_retry ON notifications(retry_at) 
    WHERE status = 'FAILED' AND retry_count < max_retries;

-- GIN indexes for JSONB columns
CREATE INDEX idx_notification_metadata_gin ON notifications USING GIN(metadata);
CREATE INDEX idx_notification_template_vars_gin ON notifications USING GIN(template_variables);
CREATE INDEX idx_email_template_vars_gin ON email_templates USING GIN(available_variables);
CREATE INDEX idx_notification_pref_category_gin ON notification_preferences USING GIN(category_preferences);
CREATE INDEX idx_notification_pref_custom_gin ON notification_preferences USING GIN(custom_preferences);

-- Specific JSONB path indexes for common queries
CREATE INDEX idx_notification_category ON notifications USING GIN((metadata->'category'));
CREATE INDEX idx_notification_action_url ON notifications USING GIN((metadata->'actionUrl'));

-- Comments for indexes
COMMENT ON INDEX idx_notification_recipient IS 'Index for finding notifications by recipient';
COMMENT ON INDEX idx_notification_unread IS 'Partial index for unread notifications query optimization';
COMMENT ON INDEX idx_notification_failed_retry IS 'Partial index for failed notifications retry processing';
COMMENT ON INDEX idx_notification_metadata_gin IS 'GIN index for metadata JSON queries';
COMMENT ON INDEX idx_notification_category IS 'GIN index for category-based notification queries';
