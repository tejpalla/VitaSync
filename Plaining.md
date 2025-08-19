# Simplified VitaSync - 2-Day Development Plan

## 🎯 Reality Check: What We're Building
A **single Spring Boot application** with basic CRUD operations for blood donation management. No microservices, no Kafka, no fancy reactive programming - just a working application that can be deployed to Azure.

---

## 🔌 Comprehensive API Design

### **User Management APIs**

#### **POST /api/users/register**
```json
Request:
{
  "name": "John Doe",
  "email": "john@email.com",
  "phone": "+1234567890",
  "bloodType": "O_POSITIVE",
  "role": "DONOR",
  "address": "123 Main St",
  "city": "Mumbai",
  "state": "Maharashtra"
}

Response: 201 Created
{
  "id": 1,
  "name": "John Doe",
  "email": "john@email.com",
  "bloodType": "O_POSITIVE",
  "role": "DONOR",
  "isAvailable": true,
  "createdAt": "2025-08-18T10:30:00Z"
}
```

#### **GET /api/users/donors?bloodType=O_POSITIVE&city=Mumbai&available=true**
```json
Response: 200 OK
{
  "content": [
    {
      "id": 1,
      "name": "John Doe",
      "bloodType": "O_POSITIVE",
      "city": "Mumbai",
      "isAvailable": true,
      "lastDonationDate": "2025-06-15",
      "phone": "+1234567890"
    }
  ],
  "totalElements": 25,
  "totalPages": 3
}
```

### **Transfusion Request APIs**

#### **POST /api/transfusion-requests**
```json
Request:
{
  "patientName": "Jane Smith",
  "bloodType": "O_POSITIVE",
  "unitsRequired": 2,
  "urgency": "HIGH",
  "hospitalName": "Apollo Hospital",
  "hospitalAddress": "Sector 26, Noida",
  "contactNumber": "+9876543210",
  "medicalReason": "Surgery complications",
  "requiredByDate": "2025-08-20T18:00:00Z"
}

Response: 201 Created
{
  "id": 101,
  "patientName": "Jane Smith",
  "bloodType": "O_POSITIVE",
  "urgency": "HIGH",
  "status": "PENDING",
  "createdAt": "2025-08-18T10:30:00Z",
  "matchingDonorsCount": 5
}
```

#### **GET /api/transfusion-requests?status=PENDING&urgency=HIGH&bloodType=O_POSITIVE**
```json
Response: 200 OK
{
  "content": [
    {
      "id": 101,
      "patientName": "Jane Smith",
      "bloodType": "O_POSITIVE",
      "unitsRequired": 2,
      "urgency": "HIGH",
      "hospitalName": "Apollo Hospital",
      "status": "PENDING",
      "requiredByDate": "2025-08-20T18:00:00Z",
      "createdAt": "2025-08-18T10:30:00Z",
      "responseCount": 3
    }
  ]
}
```

### **Donation Response APIs**

#### **POST /api/donation-responses**
```json
Request:
{
  "donorId": 1,
  "requestId": 101,
  "status": "INTERESTED",
  "donorNotes": "Available this evening",
  "scheduledDate": "2025-08-19T14:00:00Z"
}

Response: 201 Created
{
  "id": 501,
  "donorId": 1,
  "donorName": "John Doe",
  "requestId": 101,
  "status": "INTERESTED",
  "scheduledDate": "2025-08-19T14:00:00Z",
  "createdAt": "2025-08-18T10:30:00Z"
}
```

### **Matching APIs**

#### **GET /api/matching/donors-for-request/101**
```json
Response: 200 OK
{
  "requestId": 101,
  "matchingDonors": [
    {
      "donorId": 1,
      "name": "John Doe",
      "bloodType": "O_POSITIVE",
      "city": "Mumbai",
      "lastDonationDate": "2025-06-15",
      "distance": "5.2 km",
      "isAvailable": true,
      "matchScore": 95
    }
  ],
  "totalMatches": 8
}
```

#### **POST /api/matching/auto-match/101**
```json
Response: 200 OK
{
  "requestId": 101,
  "matchedDonors": [
    {"donorId": 1, "notificationSent": true},
    {"donorId": 5, "notificationSent": true}
  ],
  "message": "Notifications sent to 2 matching donors"
}
```

### **Dashboard APIs**

#### **GET /api/dashboard/stats**
```json
Response: 200 OK
{
  "totalDonors": 1250,
  "activeDonors": 890,
  "totalRequests": 156,
  "urgentRequests": 12,
  "successfulMatches": 134,
  "successRate": 85.9,
  "bloodTypeDistribution": {
    "O_POSITIVE": 350,
    "A_POSITIVE": 280,
    "B_POSITIVE": 220,
    "AB_POSITIVE": 120
  }
}
```

#### **GET /api/dashboard/urgent-requests**
```json
Response: 200 OK
{
  "criticalRequests": [
    {
      "id": 101,
      "patientName": "Jane Smith",
      "bloodType": "O_POSITIVE",
      "urgency": "CRITICAL",
      "hospitalName": "Apollo Hospital",
      "requiredByDate": "2025-08-18T20:00:00Z",
      "hoursRemaining": 6,
      "responseCount": 1
    }
  ]
}
```

---

## 📋 Simplified Requirements
- **Users** can register as donors or patients
- **Patients** can request blood transfusions
- **Donors** can see requests and respond
- **Admin** can view all data
- Basic REST API + Simple web interface (optional)
- **Dockerized** for easy Azure deployment

---

## 🏗️ Technology Stack (Simplified)
- **Language:** Java 17
- **Framework:** Spring Boot 3 (traditional MVC, not WebFlux)
- **Database:** 
  - **Development:** H2 in-memory (for quick testing)
  - **Production:** PostgreSQL via Azure Database for PostgreSQL
- **ORM:** Spring Data JPA (much easier than R2DBC)
- **Frontend:** Thymeleaf templates (optional) OR just REST API
- **Documentation:** SpringDoc OpenAPI
- **Container:** Docker

---

## 📅 2-Day Sprint Plan

### **Day 1: Core Backend (6-8 hours)**

#### Morning (3-4 hours):
1. **Project Setup (30 mins)**
   - Create Spring Boot project with dependencies:
     - Spring Web
     - Spring Data JPA
     - H2 Database (for testing)
     - PostgreSQL Driver
     - SpringDoc OpenAPI
   - Basic project structure

2. **Enhanced Database Models (1.5 hours)**
   ```
   User: id, name, email, phone, bloodType, role (DONOR/PATIENT/ADMIN), 
         address, city, state, isAvailable, lastDonationDate, createdAt, updatedAt
   
   TransfusionRequest: id, patientId, patientName, bloodType, unitsRequired, 
                      urgency (LOW/MEDIUM/HIGH/CRITICAL), hospitalName, 
                      hospitalAddress, contactNumber, medicalReason, 
                      status (PENDING/MATCHED/FULFILLED/CANCELLED), 
                      requiredByDate, createdAt, updatedAt
   
   DonationResponse: id, donorId, requestId, status (INTERESTED/CONFIRMED/COMPLETED/DECLINED),
                    donorNotes, scheduledDate, donationDate, createdAt, updatedAt
   
   BloodBank: id, name, address, phone, availableBloodTypes, operatingHours
   ```

3. **Services & Controllers Setup (1.5 hours)**
   - UserService & UserController
   - TransfusionRequestService & Controller
   - DonationResponseService & Controller
   - BloodBankService & Controller

#### Afternoon (3-4 hours):
4. **Comprehensive API Endpoints (2.5 hours)**

**User Management:**
- POST /api/users/register
- POST /api/users/login (basic auth)
- GET /api/users/{id}/profile
- PUT /api/users/{id}/profile
- PUT /api/users/{id}/availability (for donors)
- GET /api/users/donors (with filters: bloodType, city, availability)
- GET /api/users/{id}/donation-history

**Transfusion Request Management:**
- POST /api/transfusion-requests
- GET /api/transfusion-requests (with filters: bloodType, urgency, city, status)
- GET /api/transfusion-requests/{id}
- PUT /api/transfusion-requests/{id}
- DELETE /api/transfusion-requests/{id}
- GET /api/transfusion-requests/patient/{patientId}
- POST /api/transfusion-requests/{id}/cancel

**Donation Response Management:**
- POST /api/donation-responses
- GET /api/donation-responses/request/{requestId}
- GET /api/donation-responses/donor/{donorId}
- PUT /api/donation-responses/{id}/status
- POST /api/donation-responses/{id}/schedule
- POST /api/donation-responses/{id}/complete

**Matching & Discovery:**
- GET /api/matching/donors-for-request/{requestId}
- GET /api/matching/requests-for-donor/{donorId}
- POST /api/matching/auto-match/{requestId}

**Blood Bank Management:**
- GET /api/blood-banks
- GET /api/blood-banks/nearby?lat={lat}&lng={lng}
- GET /api/blood-banks/{id}/inventory

**Analytics & Dashboard:**
- GET /api/dashboard/stats
- GET /api/dashboard/urgent-requests
- GET /api/dashboard/recent-activities

5. **Validation & Error Handling (1 hour)**
   - Input validation with comprehensive rules
   - Custom exception handling
   - Proper HTTP status codes
   - Error response DTOs

6. **Testing with H2 (0.5 hours)**
   - Test critical endpoints
   - Verify data persistence

### **Day 2: Deployment Ready (6-8 hours)**

#### Morning (3-4 hours):
1. **Database Configuration (1 hour)**
   - Configure PostgreSQL for production
   - Create `application-prod.yml`
   - Environment variable configuration

2. **Docker Setup (2 hours)**
   - Create `Dockerfile`
   - Create `docker-compose.yml` (with PostgreSQL for local testing)
   - Test containerization locally

#### Afternoon (3-4 hours):
3. **Azure Preparation (1 hour)**
   - Environment variables setup
   - Database connection configuration
   - Health check endpoints

4. **Documentation (1 hour)**
   - API documentation via Swagger UI
   - Simple README with deployment instructions

5. **Final Testing (1 hour)**
   - End-to-end testing
   - Docker deployment test

6. **Buffer Time (1 hour)**
   - Fix any issues
   - Basic cleanup

---

## 🗃️ Database Strategy

### Development:
- Use **H2 in-memory** database for quick development
- No installation needed, perfect for rapid prototyping

### Production (Azure):
- **Option 1:** Azure Database for PostgreSQL (Flexible Server)
  - ~$20-30/month for basic tier
  - Managed, no maintenance needed
  
- **Option 2:** PostgreSQL in Docker container
  - Use Azure Container Instances
  - Cheaper but you manage backups

### Recommendation:
Start with H2 for development, then use Azure Database for PostgreSQL for production. It's worth the cost for a working application.

---

## 🐳 Docker Strategy

### Simple Dockerfile:
```dockerfile
FROM openjdk:17-jre-slim
COPY target/vitasync-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Azure Deployment Options:
1. **Azure Container Instances** (simplest)
2. **Azure App Service** (container deployment)
3. **Azure Container Apps** (more advanced)

---

## 🎯 Success Criteria (Minimum Viable Product)

By end of Day 2, you should have:
- ✅ Working REST API with all CRUD operations
- ✅ Dockerized application
- ✅ Database configured for production
- ✅ Deployable to Azure
- ✅ API documentation via Swagger

## 🚀 Nice-to-Have (If Time Permits)
- Simple web interface with Thymeleaf
- Basic authentication
- Email notifications (using simple JavaMail)
- Blood type matching logic

---

## 🔧 Quick Start Command Sequence

1. **Generate Project:**
   ```bash
   curl https://start.spring.io/starter.tgz \
     -d dependencies=web,data-jpa,h2,postgresql,validation \
     -d javaVersion=17 \
     -d bootVersion=3.2.0 \
     -d groupId=com.vitasync \
     -d artifactId=vitasync-app | tar -xzf -
   ```

2. **Development:**
   ```bash
   ./mvnw spring-boot:run
   # Test with H2 console at http://localhost:8080/h2-console
   ```

3. **Docker Build:**
   ```bash
   ./mvnw clean package
   docker build -t vitasync-app .
   docker run -p 8080:8080 vitasync-app
   ```

---

## 💡 Pro Tips for 2-Day Success

1. **Start with H2** - Don't waste time setting up PostgreSQL locally
2. **Use Spring Boot DevTools** - Auto-restart on code changes
3. **Test frequently** - Use Swagger UI for quick API testing
4. **Keep it simple** - No complex business logic initially
5. **Version control** - Commit working code frequently
6. **Environment variables** - Use them for database config from day 1

---

This plan gets you a **working, deployable application** in 2 days. Once it's running on Azure, you can iterate and add features incrementally!