# OpenSchool Backend Architecture Analysis

## 📋 Tổng quan

OpenSchool là một hệ thống quản lý trường học được xây dựng theo kiến trúc **Clean Architecture** với **Hexagonal Architecture pattern**. Hệ thống được phát triển bằng Java 21 và Spring Boot 3.5.3.

## 🏗️ Kiến trúc tổng thể

Hệ thống được chia thành **4 module chính**:

```
backend/
├── domain/          # Core business logic
├── application/     # Use cases và business services  
├── infrastructure/  # Technical implementation
└── spring/         # Spring Boot application entry point
```

### Dependency Flow
```
spring → infrastructure → application → domain
```

## 📁 Cấu trúc Module Chi tiết

### 1. Domain Layer (`domain/`)
**Mục đích**: Chứa business entities và domain logic thuần túy
**Dependencies**: Không có dependency nào khác
**Nguyên tắc**: Pure business logic, framework-independent

**Các domain chính**:
- `academic` - Năm học, học kỳ (AcademicYear, Semester)
- `department` - Phòng ban (Department)
- `employee` - Nhân viên (Employee, EmployeeType)
- `grade` - Khối lớp (Grade, GradeLevel, GradeStatus)
- `identity` - Xác thực người dùng (Account, Role)
- `school` - Thông tin trường học (School, SchoolType)
- `schoolclass` - Lớp học
- `systemsetup` - Thiết lập hệ thống (SystemSetupStatus, SetupStep)

**Ví dụ Domain Entity**:
```java
@Getter @Setter @AllArgsConstructor @Builder
public class School {
    private UUID id;
    private String name;
    private SchoolType type;
    private String address;
    private String phoneNumber;
    private String email;
    private String website;
    private String defaultLanguage;
    private String timezone;
}
```

### 2. Application Layer (`application/`)
**Mục đích**: Implement use cases và orchestrate business logic
**Dependencies**: Chỉ phụ thuộc vào `domain`
**Pattern**: Sử dụng **Ports & Adapters**

**Cấu trúc**:
```
application/
├── {domain}/
│   ├── port/
│   │   ├── in/     # Input Ports (Use Cases)
│   │   └── out/    # Output Ports (Repository interfaces)
│   └── service/    # Business Services
├── common/         # Shared utilities
└── exception/      # Business exceptions
```

**Input Ports (Use Cases)**:
- Định nghĩa interface cho các use case
- Ví dụ: `CreateSchoolUseCase`, `LoginUseCase`, `GetSystemSetupStatusUseCase`

**Output Ports (Repository interfaces)**:
- Định nghĩa interface cho external dependencies
- Ví dụ: `SchoolRepositoryPort`, `AccountRepositoryPort`

**Services**:
- Implement business logic và use cases
- Ví dụ: `SystemSetupService`, `LoginService`, `SchoolService`

### 3. Infrastructure Layer (`infrastructure/`)
**Mục đích**: Technical implementation và external integrations
**Dependencies**: `application` + `domain`

**Cấu trúc**:
```
infrastructure/
├── adapter/
│   ├── in/rest/    # REST Controllers
│   └── out/        # Database Adapters, External APIs
├── config/         # Spring Configuration
├── security/       # Security implementation
└── common/         # Infrastructure utilities
```

**REST Controllers**:
- `SystemSetupController` - `/api/system-setup`
- `AuthController` - `/api/public/auth`
- `DepartmentController` - `/api/departments`
- `EmployeeController` - `/api/employees`

**Database Adapters**:
- Implement repository ports
- JPA entities và repository implementations
- Ví dụ: `SchoolRepositoryAdapter`

### 4. Spring Layer (`spring/`)
**Mục đích**: Spring Boot application entry point
**Dependencies**: Chỉ `infrastructure`
**Main class**: `OpenSchoolApplication`

## 🛠️ Công nghệ Stack

### Core Technologies
- **Java**: 21
- **Framework**: Spring Boot 3.5.3
- **Build Tool**: Gradle
- **Architecture**: Clean Architecture + Hexagonal

### Database & Persistence
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA + Hibernate
- **Migration**: Liquibase
- **Connection Pool**: HikariCP (default)

### Security & Authentication
- **Framework**: Spring Security
- **Authentication**: JWT (JSON Web Token)
- **Password Encoding**: BCrypt
- **JWT Library**: jjwt 0.12.6

### Documentation & Testing
- **API Documentation**: SpringDoc OpenAPI (Swagger) 2.8.9
- **Testing**: JUnit 5 + Mockito
- **Lombok**: Code generation

### Development Tools
- **Hot Reload**: Spring Boot DevTools
- **Containerization**: Docker
- **Environment**: application-dev.yml

## 🔄 Luồng hoạt động (Request Flow)

```
1. HTTP Request → REST Controller (Infrastructure)
2. Controller → Use Case Interface (Application)
3. Use Case → Domain Service → Repository Port
4. Repository Port → Repository Adapter (Infrastructure)
5. JPA Repository → Database
6. Response ← Controller ← Use Case ← Repository
```

## 🎯 Các tính năng chính

### 1. System Setup (Thiết lập hệ thống)
- **Endpoint**: `/api/system-setup`
- **Flow**: CREATE_ADMIN_USER → CREATE_SCHOOL → CREATE_ACADEMIC_YEAR → CREATE_GRADES → FINISH
- **Features**:
  - Tạo admin user đầu tiên
  - Thiết lập thông tin trường học
  - Tạo năm học
  - Thiết lập các khối lớp

### 2. Authentication (Xác thực)
- **Endpoint**: `/api/public/auth/login`
- **Method**: JWT-based authentication
- **Features**:
  - Login với username/password
  - JWT token generation
  - Password encryption với BCrypt

### 3. School Management
- **Domain**: School, SchoolType
- **Features**: Quản lý thông tin trường học

### 4. Department Management
- **Endpoint**: `/api/departments`
- **Features**:
  - CRUD operations cho phòng ban
  - Quản lý nhân viên trong phòng ban
  - Pagination support

### 5. Employee Management
- **Endpoint**: `/api/employees`
- **Features**:
  - CRUD operations cho nhân viên
  - Phân loại theo EmployeeType
  - Gán vào phòng ban

### 6. Academic Management
- **Features**:
  - Quản lý năm học (AcademicYear)
  - Quản lý học kỳ (Semester)
  - Quản lý khối lớp (Grade)

## 📊 Database Schema

### Migration Management
- **Tool**: Liquibase
- **Master file**: `db/changelog/changelog-master.yaml`
- **Modules**:
  - identity (users, roles)
  - account (user accounts)
  - department
  - systemsetup
  - school
  - academic
  - grade
  - employee

## 🔧 Configuration

### Database Configuration (Dev)
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/openschool
    username: openschool_user
    password: openschool_pass
  jpa:
    hibernate:
      ddl-auto: none
  liquibase:
    change-log: classpath:/db/changelog/changelog-master.yaml
```

### Security Configuration
```yaml
security:
  jwt:
    secret: "VGhpcyBpcyBhIHNhbXBsZSBzZWNyZXQga2V5IHdpdGggMzIgYnl0ZXMgcGFkZGVkLg=="
    expiration-in-ms: 86400000
```

## ✅ Ưu điểm của kiến trúc

### 1. Separation of Concerns
- Business logic tách biệt khỏi technical details
- Domain layer không phụ thuộc vào framework
- Clear boundaries giữa các layer

### 2. Testability
- Dependency injection cho easy mocking
- Pure business logic dễ unit test
- Port interfaces cho integration testing

### 3. Maintainability
- Code structure rõ ràng và nhất quán
- Easy to locate và modify features
- Loose coupling giữa các components

### 4. Flexibility
- Dễ thay đổi database implementation
- Có thể swap framework mà không ảnh hưởng business logic
- Plugin architecture với ports & adapters

### 5. Scalability
- Modular design cho team development
- Clear API contracts
- Independent deployment của các modules

## 🚀 Deployment

### Docker Support
```dockerfile
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/spring/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Build Commands
```bash
# Build all modules
./gradlew build

# Build Spring Boot jar
./gradlew :spring:bootJar

# Run tests
./gradlew test
```

## 📝 Best Practices Implemented

1. **Clean Architecture principles**
2. **SOLID principles**
3. **Dependency Inversion**
4. **Port & Adapter pattern**
5. **Domain-Driven Design (DDD)**
6. **Command Query Responsibility Segregation (CQRS) elements**
7. **Proper exception handling**
8. **Comprehensive testing strategy**

---

**Generated on**: 2025-08-18
**Analyzed by**: Augment Agent
**Codebase**: OpenSchool Backend v0.0.1-SNAPSHOT
