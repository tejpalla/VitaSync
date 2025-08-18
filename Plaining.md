# VitaSync - Java 21 + Gradle Implementation Plan

## 🚀 Spring Initializer Configuration

### **Exact Configuration:**
- **Project:** Gradle - Groovy
- **Language:** Java
- **Spring Boot:** 3.3.2 (latest stable)
- **Project Metadata:**
  - Group: `com.vitasync`
  - Artifact: `vitasync-app`
  - Name: `VitaSync`
  - Description: `Blood Donation Management System`
  - Package name: `com.vitasync`
  - Packaging: `Jar`
  - **Java Version: 21**

### **Dependencies to Select:**
```
Web:
- Spring Web
- Spring Boot DevTools

Data:
- Spring Data JPA
- H2 Database
- PostgreSQL Driver
- Validation

Documentation:
- Spring Boot Actuator (for health checks)
- Springdoc OpenAPI UI
```

### **Direct URL for Spring Initializer:**
```
https://start.spring.io/#!type=gradle-project-groovy&language=java&platformVersion=3.3.2&packaging=jar&jvmVersion=21&groupId=com.vitasync&artifactId=vitasync-app&name=VitaSync&description=Blood%20Donation%20Management%20System&packageName=com.vitasync&dependencies=web,devtools,data-jpa,h2,postgresql,validation,actuator
```

---

## 📁 Complete Folder Structure

```
vitasync-app/
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
├── gradle/
│   └── wrapper/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── vitasync/
│       │           ├── VitaSyncApplication.java
│       │           ├── config/
│       │           │   ├── DatabaseConfig.java
│       │           │   ├── OpenApiConfig.java
│       │           │   └── CorsConfig.java
│       │           ├── controller/
│       │           │   ├── UserController.java
│       │           │   ├── TransfusionRequestController.java
│       │           │   ├── DonationResponseController.java
│       │           │   ├── MatchingController.java
│       │           │   ├── BloodBankController.java
│       │           │   └── DashboardController.java
│       │           ├── service/
│       │           │   ├── UserService.java
│       │           │   ├── TransfusionRequestService.java
│       │           │   ├── DonationResponseService.java
│       │           │   ├── MatchingService.java
│       │           │   ├── BloodBankService.java
│       │           │   └── DashboardService.java
│       │           ├── repository/
│       │           │   ├── UserRepository.java
│       │           │   ├── TransfusionRequestRepository.java
│       │           │   ├── DonationResponseRepository.java
│       │           │   └── BloodBankRepository.java
│       │           ├── entity/
│       │           │   ├── User.java
│       │           │   ├── TransfusionRequest.java
│       │           │   ├── DonationResponse.java
│       │           │   └── BloodBank.java
│       │           ├── dto/
│       │           │   ├── request/
│       │           │   │   ├── UserRegistrationRequest.java
│       │           │   │   ├── TransfusionRequestDto.java
│       │           │   │   ├── DonationResponseDto.java
│       │           │   │   └── UpdateAvailabilityRequest.java
│       │           │   ├── response/
│       │           │   │   ├── UserProfileResponse.java
│       │           │   │   ├── TransfusionRequestResponse.java
│       │           │   │   ├── DashboardStatsResponse.java
│       │           │   │   ├── MatchingDonorResponse.java
│       │           │   │   └── ApiResponse.java
│       │           │   └── common/
│       │           │       ├── PagedResponse.java
│       │           │       └── ErrorResponse.java
│       │           ├── enums/
│       │           │   ├── BloodType.java
│       │           │   ├── UserRole.java
│       │           │   ├── UrgencyLevel.java
│       │           │   ├── RequestStatus.java
│       │           │   └── DonationStatus.java
│       │           ├── exception/
│       │           │   ├── GlobalExceptionHandler.java
│       │           │   ├── ResourceNotFoundException.java
│       │           │   ├── ValidationException.java
│       │           │   └── BusinessException.java
│       │           └── util/
│       │               ├── BloodTypeCompatibility.java
│       │               ├── DistanceCalculator.java
│       │               └── DateUtils.java
│       └── resources/
│           ├── application.yml
│           ├── application-dev.yml
│           ├── application-prod.yml
│           ├── data.sql (sample data)
│           └── static/ (if adding web UI)
│               └── swagger-ui/
├── src/
│   └── test/
│       └── java/
│           └── com/
│               └── vitasync/
│                   ├── VitaSyncApplicationTests.java
│                   ├── controller/
│                   ├── service/
│                   └── repository/
├── Dockerfile
├── docker-compose.yml
└── README.md
```

---

## 📝 Enhanced build.gradle

```gradle
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.3.2'
    id 'io.spring.dependency-management' version '1.1.6'
}

group = 'com.vitasync'
version = '0.0.1-SNAPSHOT'

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot Core
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
    
    // Database
    runtimeOnly 'com.h2database:h2'
    runtimeOnly 'org.postgresql:postgresql'
    
    // Documentation
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0'
    
    // Development Tools
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
    
    // Annotation Processing
    annotationProcessor 'org.springframework.boot:spring-boot-configuration-processor'
    
    // Test Dependencies
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.springframework.boot:spring-boot-testcontainers'
    testImplementation 'org.testcontainers:junit-jupiter'
    testImplementation 'org.testcontainers:postgresql'
}

tasks.named('test') {
    useJUnitPlatform()
}
```

---

## ⚙️ Configuration Files

### **application.yml**
```yaml
spring:
  application:
    name: VitaSync
  
  profiles:
    active: dev
    
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        
  h2:
    console:
      enabled: true
      path: /h2-console
      
server:
  port: 8080
  
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
        
springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
    operations-sorter: method
```

### **application-dev.yml**
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:vitasync_dev
    driverClassName: org.h2.Driver
    username: sa
    password: password
    
  jpa:
    defer-datasource-initialization: true
    
  sql:
    init:
      mode: always
      
logging:
  level:
    com.vitasync: DEBUG
    org.springframework.web: DEBUG
```

### **application-prod.yml**
```yaml
spring:
  datasource:
    url: ${DATABASE_URL:jdbc:postgresql://localhost:5432/vitasync}
    username: ${DATABASE_USERNAME:vitasync_user}
    password: ${DATABASE_PASSWORD:your_password}
    
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    
  h2:
    console:
      enabled: false
      
server:
  port: ${PORT:8080}
  
logging:
  level:
    com.vitasync: INFO
    org.springframework: WARN
```

---

## 🐳 Docker Configuration

### **Dockerfile**
```dockerfile
# Build stage
FROM gradle:8.5-jdk21 AS build
WORKDIR /app
COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle
COPY src src
RUN ./gradlew build -x test

# Run stage
FROM openjdk:21-jre-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### **docker-compose.yml**
```yaml
version: '3.8'
services:
  vitasync-app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DATABASE_URL=jdbc:postgresql://postgres:5432/vitasync
      - DATABASE_USERNAME=vitasync_user
      - DATABASE_PASSWORD=secure_password
    depends_on:
      - postgres
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 5

  postgres:
    image: postgres:15-alpine
    environment:
      - POSTGRES_DB=vitasync
      - POSTGRES_USER=vitasync_user
      - POSTGRES_PASSWORD=secure_password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U vitasync_user -d vitasync"]
      interval: 10s
      timeout: 5s
      retries: 5

volumes:
  postgres_data:
```

---

## 🎯 Development Workflow

### **Step 1: Initialize Project**
```bash
# Download from Spring Initializer using the URL above
# Or use the curl command:
curl -G https://start.spring.io/starter.zip \
  -d type=gradle-project-groovy \
  -d language=java \
  -d platformVersion=3.3.2 \
  -d packaging=jar \
  -d jvmVersion=21 \
  -d groupId=com.vitasync \
  -d artifactId=vitasync-app \
  -d name=VitaSync \
  -d dependencies=web,devtools,data-jpa,h2,postgresql,validation,actuator \
  -o vitasync.zip

unzip vitasync.zip
cd vitasync-app
```

### **Step 2: Development Commands**
```bash
# Run in development mode (with H2)
./gradlew bootRun --args='--spring.profiles.active=dev'

# Run tests
./gradlew test

# Build JAR
./gradlew build

# Run with Docker
docker-compose up --build

# Access H2 Console (dev mode)
http://localhost:8080/h2-console

# Access Swagger UI
http://localhost:8080/swagger-ui.html

# Health check
http://localhost:8080/actuator/health
```

---

## 📅 2-Day Implementation Timeline

### **Day 1 (Focus: Core Models & APIs)**
1. **Morning:** Setup project, create entities and enums
2. **Afternoon:** Implement repositories, services, and basic controllers

### **Day 2 (Focus: Business Logic & Deployment)**
1. **Morning:** Implement matching logic, validation, and exception handling
2. **Afternoon:** Docker setup, testing, and Azure deployment prep

This structure gives you a **professional, maintainable Spring Boot application** that's ready for enterprise use! 

**Ready to start with the entity classes?**