# OpenSchool Notification System

## Tổng quan

Hệ thống thông báo của OpenSchool được xây dựng theo kiến trúc Clean Architecture với Hexagonal pattern, cung cấp khả năng gửi email và tin nhắn nội bộ một cách linh hoạt và có thể mở rộng.

## Tính năng chính

### 📧 Email Notifications
- Gửi email đơn lẻ và hàng loạt
- Hỗ trợ email template với biến động
- Gửi email HTML và plain text
- Hỗ trợ CC, BCC
- Retry mechanism cho email thất bại
- Validation email address

### 💬 Internal Messages
- Tin nhắn nội bộ trong hệ thống
- Gửi tin nhắn đơn lẻ và hàng loạt
- Real-time notifications qua WebSocket
- Push notifications
- Phân loại theo category và priority

### 🎨 Email Templates
- Quản lý template email
- Hỗ trợ biến động (variables)
- Versioning cho templates
- Multi-language support
- Template activation/deactivation

### ⚙️ User Preferences
- Cài đặt thông báo cá nhân
- Bật/tắt từng loại thông báo
- Quiet hours (giờ im lặng)
- Digest mode (gộp thông báo)
- Category-specific preferences

## Kiến trúc

### Domain Layer
```
domain/notification/
├── Notification.java           # Core notification entity
├── NotificationType.java       # Email, Internal Message, Push, SMS
├── NotificationStatus.java     # Pending, Sent, Read, Failed, etc.
├── NotificationPriority.java   # Low, Normal, High, Critical
├── EmailTemplate.java          # Email template entity
└── NotificationPreference.java # User preferences
```

### Application Layer
```
application/notification/
├── port/in/                    # Use cases (interfaces)
│   ├── SendEmailUseCase.java
│   ├── SendInternalMessageUseCase.java
│   ├── GetNotificationsUseCase.java
│   ├── MarkAsReadUseCase.java
│   └── command/                # Command objects
├── port/out/                   # Repository interfaces
│   ├── NotificationRepositoryPort.java
│   ├── EmailServicePort.java
│   └── PushNotificationServicePort.java
├── service/                    # Business logic
│   ├── NotificationService.java
│   ├── EmailTemplateService.java
│   └── NotificationPreferenceService.java
└── exception/                  # Business exceptions
```

### Infrastructure Layer
```
infrastructure/notification/
├── entity/                     # JPA entities
├── repository/                 # JPA repositories & adapters
├── email/                      # Email service implementation
├── controller/                 # REST controllers
└── config/                     # Spring configuration
```

## API Endpoints

### Notifications
- `GET /api/notifications/user/{userId}` - Lấy thông báo của user
- `GET /api/notifications/user/{userId}/unread` - Thông báo chưa đọc
- `GET /api/notifications/user/{userId}/unread-count` - Đếm thông báo chưa đọc
- `POST /api/notifications/internal-message` - Gửi tin nhắn nội bộ
- `PUT /api/notifications/{id}/mark-as-read` - Đánh dấu đã đọc

### Email Templates (Admin)
- `GET /api/admin/email-templates` - Danh sách templates
- `POST /api/admin/email-templates` - Tạo template mới
- `PUT /api/admin/email-templates/{id}` - Cập nhật template
- `DELETE /api/admin/email-templates/{id}` - Xóa template

### User Preferences
- `GET /api/users/{userId}/notification-preferences` - Lấy cài đặt
- `PUT /api/users/{userId}/notification-preferences` - Cập nhật cài đặt

## Database Schema

### notifications
```sql
CREATE TABLE notifications (
    id UUID PRIMARY KEY,
    recipient_id UUID NOT NULL,
    sender_id UUID,
    title VARCHAR(500) NOT NULL,
    content TEXT,
    type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    priority VARCHAR(50) NOT NULL,
    email VARCHAR(255),
    template_id UUID,
    template_variables JSONB,
    metadata JSONB,
    created_at TIMESTAMP NOT NULL,
    sent_at TIMESTAMP,
    read_at TIMESTAMP,
    -- ... other fields
);
```

### email_templates
```sql
CREATE TABLE email_templates (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    display_name VARCHAR(200),
    subject VARCHAR(500) NOT NULL,
    html_content TEXT,
    text_content TEXT,
    available_variables JSONB,
    active BOOLEAN NOT NULL,
    category VARCHAR(50),
    language VARCHAR(10),
    -- ... other fields
);
```

### notification_preferences
```sql
CREATE TABLE notification_preferences (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE,
    email_enabled BOOLEAN NOT NULL,
    internal_message_enabled BOOLEAN NOT NULL,
    push_notification_enabled BOOLEAN NOT NULL,
    category_preferences JSONB,
    -- ... other fields
);
```

## Cách sử dụng

### 1. Gửi Email
```java
@Autowired
private SendEmailUseCase sendEmailUseCase;

SendEmailCommand command = SendEmailCommand.builder()
    .to("user@example.com")
    .subject("Welcome!")
    .content("Welcome to OpenSchool!")
    .isHtml(false)
    .recipientId(userId)
    .priority(NotificationPriority.NORMAL)
    .build();

Notification notification = sendEmailUseCase.sendEmail(command);
```

### 2. Gửi Tin nhắn nội bộ
```java
@Autowired
private SendInternalMessageUseCase sendInternalMessageUseCase;

SendInternalMessageCommand command = SendInternalMessageCommand.builder()
    .recipientId(userId)
    .title("New Grade Available")
    .content("Your grade for Math has been updated")
    .priority(NotificationPriority.HIGH)
    .sendPushNotification(true)
    .build();

Notification notification = sendInternalMessageUseCase.sendInternalMessage(command);
```

### 3. Sử dụng Email Template
```java
SendEmailCommand command = SendEmailCommand.builder()
    .to("user@example.com")
    .templateId(templateId)
    .templateVariables(Map.of(
        "userName", "John Doe",
        "schoolName", "OpenSchool Academy"
    ))
    .recipientId(userId)
    .build();
```

### 4. WebSocket Real-time Notifications
```javascript
// Frontend JavaScript
const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
    // Subscribe to user-specific notifications
    stompClient.subscribe('/user/' + userId + '/notifications', function(message) {
        const notification = JSON.parse(message.body);
        showNotification(notification);
    });
    
    // Subscribe to broadcast notifications
    stompClient.subscribe('/topic/notifications', function(message) {
        const notification = JSON.parse(message.body);
        showBroadcastNotification(notification);
    });
});
```

## Configuration

### Email Configuration (application-dev.yml)
```yaml
spring:
  mail:
    host: localhost
    port: 1025
    username: 
    password: 
    properties:
      mail:
        smtp:
          auth: false
          starttls:
            enable: false
```

### Production Email Configuration
```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${EMAIL_USERNAME}
    password: ${EMAIL_PASSWORD}
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

## Testing

### Unit Tests
- `NotificationServiceTest` - Test business logic
- `EmailTemplateServiceTest` - Test template management
- `NotificationRepositoryAdapterTest` - Test data access

### Integration Tests
```bash
# Run all notification tests
./gradlew test --tests "*notification*"

# Run specific test class
./gradlew test --tests "NotificationServiceTest"
```

## Deployment

### 1. Database Migration
```bash
# Migrations sẽ tự động chạy khi start application
# Hoặc chạy manual:
./gradlew liquibaseUpdate
```

### 2. Email Service Setup
- Cấu hình SMTP server
- Thiết lập authentication
- Test email connectivity

### 3. WebSocket Configuration
- Cấu hình CORS cho WebSocket
- Load balancer sticky sessions (nếu có)

## Monitoring & Troubleshooting

### Logs
```bash
# Check notification logs
tail -f logs/application.log | grep "notification"

# Check email sending logs
tail -f logs/application.log | grep "email"
```

### Metrics
- Email success/failure rates
- Notification delivery times
- User engagement metrics
- Template usage statistics

### Common Issues
1. **Email không gửi được**: Kiểm tra SMTP configuration
2. **WebSocket không connect**: Kiểm tra CORS settings
3. **Template không load**: Kiểm tra template active status
4. **Performance chậm**: Kiểm tra database indexes

## Roadmap

### Phase 2
- [ ] SMS notifications
- [ ] Mobile push notifications (FCM/APNs)
- [ ] Advanced template editor
- [ ] Notification analytics dashboard
- [ ] A/B testing cho templates

### Phase 3
- [ ] Machine learning cho personalization
- [ ] Advanced scheduling
- [ ] Multi-tenant support
- [ ] API rate limiting
- [ ] Notification campaigns

## Contributing

1. Follow Clean Architecture principles
2. Write comprehensive unit tests
3. Update documentation
4. Follow naming conventions
5. Add proper error handling

## Support

Để được hỗ trợ, vui lòng tạo issue trên GitHub repository hoặc liên hệ team phát triển.
