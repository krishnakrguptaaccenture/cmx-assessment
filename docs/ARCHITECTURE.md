# CMX Assessment - Chubb APAC Claims Processing System
## Backend Architecture Document

**Version:** 1.0.0  
**Status:** For Review  
**Last Updated:** 2026-07-31

---

## 1. EXECUTIVE SUMMARY

CMX Assessment is a **modular monolith** built with Spring Boot 3.x on Java 17 that processes motor and property insurance claims across six APAC markets. The system supports three distinct user personas with clear workflows:

- **Claimants**: Report incidents and track claims
- **Claims Staff**: Review, assess, and make approval/rejection decisions
- **Managers**: Monitor real-time workload and team performance

The service flow spans from incident reporting through claim assessment and decision, with all decisions published to Kafka for downstream services (notifications, payments, etc.).

---

## 2. ARCHITECTURAL APPROACH: MODULAR MONOLITH

### Why Modular Monolith?

A modular monolith provides the best of both worlds:
- **Single deployable unit** - simpler operations and deployment pipeline
- **Clear module boundaries** - enforces separation of concerns
- **Decoupled modules** - can evolve independently via events
- **Future-proof** - can extract modules to microservices later

### Key Principle

Each module owns its data, exposes a well-defined interface (REST API + Events), and communicates with other modules through:
- **Synchronous**: REST calls for immediate responses
- **Asynchronous**: Kafka events for eventual consistency and decoupling

---

## 3. MODULE STRUCTURE

### 3.1 COMMON MODULE (Shared Infrastructure)
**Location:** `com.chubb.apac.claims.modulith.common`

Provides cross-cutting concerns used by all modules:
- `config/` - Spring configuration, security config, Kafka config
- `dto/` - Shared DTOs (responses, error messages)
- `enums/` - Common enumerations (UserRole, ClaimStatus, ProductType, Market)
- `exception/` - Global exception handling
- `security/` - JWT token utilities, SecurityContext helpers
- `util/` - Helper utilities

**Key Classes:**
- `SecurityConfig` - JWT token generation/validation
- `GlobalExceptionHandler` - REST error responses
- `BaseEntity` - JPA audit fields (createdAt, updatedAt, createdBy)

---

### 3.2 USER MODULE (Authentication & User Management)
**Location:** `com.chubb.apac.claims.modulith.user`

Manages user accounts, authentication, and authorization.

**Entities:**
- `User` - username, email, password, full name, active status
- `UserRole` - CLAIMANT, CLAIMS_STAFF, MANAGER
- `UserMarket` - User's assigned market(s) for staff/managers
- `UserTeam` - Team assignment for claims staff

**REST Endpoints:**
```
POST   /api/v1/auth/register          - Claimant self-registration
POST   /api/v1/auth/login             - Generate JWT token
POST   /api/v1/auth/logout            - Invalidate token
GET    /api/v1/users/profile          - Get current user profile
PUT    /api/v1/users/profile          - Update profile
POST   /api/v1/staff/users            - Create staff user (Manager only)
GET    /api/v1/staff/users            - List staff users (Manager only)
```

**Database Tables:**
- `users`
- `user_roles`
- `user_markets`
- `user_teams`

---

### 3.3 INCIDENT MODULE (Claim Initiation)
**Location:** `com.chubb.apac.claims.modulith.incident`

Handles incident reporting and incident party management.

**Entities:**
- `Incident` - incidentId, reportDate, type, location, description, market
- `IncidentParty` - Type (CLAIMANT, THIRD_PARTY, WITNESS), contact info, statement
- `ClaimItem` - itemType (MOTOR_VEHICLE, PROPERTY, PERSON), description, value

**REST Endpoints:**
```
POST   /api/v1/incidents                           - Report new incident (Claimant)
GET    /api/v1/incidents/{incidentId}             - Get incident details (Claimant)
PUT    /api/v1/incidents/{incidentId}             - Update incident (Claimant)
POST   /api/v1/incidents/{incidentId}/parties     - Add incident parties (Claimant)
GET    /api/v1/incidents/{incidentId}/parties     - List incident parties (Claimant)
```

**Events Published:**
- `IncidentReported` → Triggers claim creation in Claim module
- `IncidentUpdated` → Notifies assessment team

**Database Tables:**
- `incidents`
- `incident_parties`
- `claim_items`

---

### 3.4 CLAIM MODULE (Central Orchestrator)
**Location:** `com.chubb.apac.claims.modulith.claim`

Central hub for claim lifecycle and state management.

**Entities:**
- `Claim` - claimId, incidentId, claimantId, productType (MOTOR, PROPERTY), status, market
- `ClaimStatus` - REPORTED, UNDER_REVIEW, AWAITING_INFORMATION, READY_FOR_DECISION, APPROVED, REJECTED
- `ClaimStatusHistory` - Audit trail of status changes with reason

**REST Endpoints:**
```
GET    /api/v1/claims/{claimId}                   - Get claim details (Claimant)
GET    /api/v1/claims/{claimId}/status            - Get current status (Claimant)
GET    /api/v1/claimants/{claimantId}/claims      - List claimant's claims (Claimant)
GET    /api/v1/staff/claims                       - List all claims (Claims Staff/Manager)
GET    /api/v1/staff/claims/{claimId}             - Get claim for assessment (Claims Staff)
POST   /api/v1/staff/claims/{claimId}/assign      - Assign claim to self (Claims Staff)
POST   /api/v1/staff/claims/{claimId}/unassign    - Unassign claim (Claims Staff)
```

**Events Published:**
- `ClaimCreated` → Updates workload, audit
- `ClaimStatusChanged` → Triggers notifications, workload updates

**Events Consumed:**
- `IncidentReported` → Creates new Claim

**Database Tables:**
- `claims`
- `claim_status_history`
- `claim_assignments`

---

### 3.5 ASSESSMENT MODULE (Claims Review & Decision)
**Location:** `com.chubb.apac.claims.modulith.assessment`

Handles claim assessment, validation, and decision-making.

**Entities:**
- `Assessment` - claimId, assessorId, findingsText, recommendedDecision, submissionDate
- `AssessmentStatus` - IN_PROGRESS, SUBMITTED, APPROVED_BY_MANAGER, REJECTED_BY_MANAGER
- `DecisionRecord` - claimId, decision (APPROVED/REJECTED), decisionReason, decisionDate, deciderId
- `ValidationRule` - market, productType, rule condition, rule text

**REST Endpoints:**
```
POST   /api/v1/staff/assessments/{claimId}        - Submit assessment (Claims Staff)
GET    /api/v1/staff/assessments/{claimId}        - Get assessment details (Claims Staff)
PUT    /api/v1/staff/assessments/{claimId}        - Update assessment (Claims Staff)
POST   /api/v1/staff/assessments/{claimId}/validate - Validate assessment (Claims Staff)
GET    /api/v1/staff/assessments/pending          - List pending assessments (Manager)
POST   /api/v1/staff/decisions/{claimId}/approve  - Approve claim decision (Manager)
POST   /api/v1/staff/decisions/{claimId}/reject   - Reject claim decision (Manager)
GET    /api/v1/staff/decisions/{claimId}          - View decision (Staff/Manager)
```

**Events Published:**
- `AssessmentSubmitted` → Ready for management review
- `ValidationFailed` → Sends feedback to claims staff
- `DecisionMade` → APPROVED or REJECTED decision published to Kafka
  - **Payload**: `{ claimId, decision, reason, deciderId, decisionDate, claimantEmail, amount }`
  - **Topic**: `claim.decisions` (for external services: notifications, payment processing)

**Database Tables:**
- `assessments`
- `assessment_status_history`
- `decision_records`
- `validation_rules`

---

### 3.6 INFORMATION REQUEST MODULE (Claimant Engagement)
**Location:** `com.chubb.apac.claims.modulith.inforequest`

Manages requests for additional information and claimant responses.

**Entities:**
- `InformationRequest` - claimId, requestedFields (comma-separated), createdBy, dueDate, status
- `InformationRequestStatus` - PENDING, SUBMITTED, RECEIVED, CLARIFICATION_NEEDED
- `InformationResponse` - requestId, claimantResponse, submissionDate

**REST Endpoints:**
```
GET    /api/v1/claims/{claimId}/info-requests         - List requests for claimant (Claimant)
POST   /api/v1/claims/{claimId}/info-requests/{requestId}/submit - Submit response (Claimant)
POST   /api/v1/staff/claims/{claimId}/info-requests   - Create request (Claims Staff)
GET    /api/v1/staff/claims/{claimId}/info-requests   - View requests (Claims Staff)
```

**Events Published:**
- `InformationRequested` → Notifies claimant
- `InformationSubmitted` → Notifies claims staff

**Database Tables:**
- `information_requests`
- `information_responses`

---

### 3.7 WORKLOAD MODULE (Team Management)
**Location:** `com.chubb.apac.claims.modulith.workload`

Tracks team and individual performance metrics.

**Entities:**
- `TeamWorkload` - teamId, assignedClaimsCount, avgResolutionTime, updateTime
- `PerformanceMetric` - assessorId, claimsProcessed, avgProcessingTime, approvalRate, updateTime
- `SLATracker` - claimId, assignedDate, slaDeadline, slaStatus (ON_TRACK, AT_RISK, BREACHED)

**REST Endpoints:**
```
GET    /api/v1/staff/dashboard/workload             - Team workload summary (Manager)
GET    /api/v1/staff/dashboard/performance          - Staff performance metrics (Manager)
GET    /api/v1/staff/dashboard/sla-status           - SLA tracking dashboard (Manager)
GET    /api/v1/staff/team/{teamId}/claims-assigned  - Team's assigned claims (Manager)
```

**Events Consumed:**
- `ClaimAssigned` → Updates workload count
- `AssessmentSubmitted` → Updates SLA tracker
- `DecisionMade` → Updates performance metrics

**Database Tables:**
- `team_workload`
- `performance_metrics`
- `sla_tracking`

---

### 3.8 CONFIGURATION MODULE (Market & Business Rules)
**Location:** `com.chubb.apac.claims.modulith.config`

Multi-market configuration and business rules.

**Entities:**
- `Market` - marketId (SG, MY, TH, ID, PH, VN), name, currency, language, timezone
- `ProductType` - market, type (MOTOR, PROPERTY), claimLimitMin, claimLimitMax
- `SLAConfiguration` - market, productType, assessmentSLADays, decisionSLADays
- `BusinessRule` - market, productType, ruleKey, ruleValue, description

**REST Endpoints:**
```
GET    /api/v1/config/markets                  - List supported markets (Public)
GET    /api/v1/config/products/{market}        - List products for market (Public)
GET    /api/v1/config/rules/{market}/{product} - Get business rules (Public)
```

**Database Tables:**
- `markets`
- `product_types`
- `sla_configurations`
- `business_rules`

---

### 3.9 NOTIFICATION MODULE (Communication)
**Location:** `com.chubb.apac.claims.modulith.notification`

Publishes notifications (external service responsibility).

**Note:** Notification Module does NOT consume Kafka events directly in this service. It provides notification templates and logging. External notification service subscribes to Kafka topics and sends emails/SMS/in-app messages.

**Service Layer Only:**
- `NotificationService` - Constructs notification messages
- `NotificationTemplate` - Email/SMS templates per event type

**Database Tables:**
- `notification_templates`
- `notification_log` (optional, for audit)

---

### 3.10 AUDIT MODULE (Compliance & Logging)
**Location:** `com.chubb.apac.claims.modulith.audit`

Comprehensive audit trail for all claim actions.

**Entities:**
- `AuditLog` - entityType, entityId, action (CREATE, UPDATE, DELETE, DECISION), userId, timestamp, changes (JSON)

**Characteristics:**
- Automatically populated via JPA event listeners or aspect-oriented programming
- Captures all claim status changes, assessments, decisions
- Immutable records for regulatory compliance

**Database Tables:**
- `audit_logs`

---

### 3.11 KAFKA MODULE (Event Infrastructure)
**Location:** `com.chubb.apac.claims.modulith.kafka`

Central event producer/consumer configuration.

**Components:**
- `KafkaConfig` - Kafka producer/consumer beans
- `KafkaTopicConfig` - Topic creation and configuration
- `ClaimEventProducer` - Publishes events to Kafka
- Event listeners for consuming cross-module events

**Kafka Topics:**
| Topic | Partition Key | Consumers | Purpose |
|-------|---------------|-----------|---------|
| `incident.events` | incidentId | claim-module, audit | Incident reported/updated |
| `claim.events` | claimId | workload, audit | Claim created, status changed |
| `inforequest.events` | claimId | notification, audit | Info requested/submitted |
| `assessment.events` | claimId | audit, workload | Assessment submitted |
| `claim.decisions` | claimId | **external services** | APPROVED/REJECTED decisions |

---

## 4. REST API DESIGN

### 4.1 API Versioning
All endpoints use `/api/v1/` prefix. Backward compatibility is maintained in future versions.

### 4.2 Request/Response Format

**Standard Response Envelope:**
```json
{
  "success": true,
  "data": { /* payload */ },
  "timestamp": "2026-07-31T13:35:11Z"
}
```

**Error Response:**
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Field 'email' is required",
    "details": [
      { "field": "email", "message": "must not be blank" }
    ]
  },
  "timestamp": "2026-07-31T13:35:11Z"
}
```

### 4.3 HTTP Status Codes
- `200` - Successful request
- `201` - Resource created
- `400` - Validation error
- `401` - Unauthorized (missing/invalid JWT)
- `403` - Forbidden (insufficient permissions)
- `404` - Resource not found
- `409` - Conflict (e.g., claim already assigned)
- `500` - Internal server error

### 4.4 Authentication
- All endpoints require JWT Bearer token in `Authorization` header
- Claimants authenticate via `/api/v1/auth/login` with email/password
- Staff/Managers authenticated separately (via identity provider integration in future)
- Token expiration: configurable (default 24 hours)

### 4.5 Authorization
Role-based access control:
- **CLAIMANT** - Can view own claims, submit info, report incidents
- **CLAIMS_STAFF** - Can view all claims, assign to self, submit assessments, request info
- **MANAGER** - Can view all claims, approve/reject decisions, view team performance

---

## 5. DATA MODEL PRINCIPLES

### 5.1 Multi-Tenancy via Market
- All entities have a `market` field
- Queries are filtered by market (security boundary)
- Claims staff/managers have explicit market assignments

### 5.2 Audit Trail
- All auditable entities extend `BaseEntity` (createdAt, updatedAt, createdBy)
- All status changes logged in separate history tables
- All decisions captured in immutable `decision_records`

### 5.3 Claim Workflow States
```
REPORTED
    ↓
UNDER_REVIEW
    ↓
AWAITING_INFORMATION (if more info needed)
    ↓
READY_FOR_DECISION
    ↓
APPROVED / REJECTED
```

---

## 6. EVENT-DRIVEN ARCHITECTURE

### 6.1 Event Publishing Strategy
- All domain-significant events published to Kafka
- Events use domain object IDs (not UUIDs) as partition keys for ordering
- All events include timestamp, source module, correlation ID

### 6.2 Event Payload Example: DecisionMade
```json
{
  "eventType": "DecisionMade",
  "claimId": "CLM-2026-000123",
  "decision": "APPROVED",
  "decisionReason": "All documents verified, damage assessed within policy limits",
  "deciderId": "MGR001",
  "decisionDate": "2026-07-31T13:35:11Z",
  "claimantEmail": "john.doe@example.com",
  "approvedAmount": 50000.00,
  "market": "SG",
  "productType": "MOTOR",
  "correlationId": "req-12345-67890"
}
```

### 6.3 Kafka Consumer Pattern
- Each module has a dedicated event consumer service
- Consumers use Spring Kafka listeners with exception handling
- Failed events go to dead-letter topic for manual review

---

## 7. SECURITY ARCHITECTURE

### 7.1 Authentication
- **JWT Bearer Token** - All API requests
- **Token Claims**: userId, email, role, market, teamId, expiresAt

### 7.2 Authorization
- **Role-Based Access Control (RBAC)** - @Secured or @PreAuthorize annotations
- **Data-Level Security** - Market and claimant ID filters in queries
- **Method-Level Security** - Service layer validation

### 7.3 Secrets Management
- JWT secret stored in environment variable (production)
- H2 database password in application.yml (dev only)
- Kafka credentials via Spring Cloud Config (future enhancement)

### 7.4 Input Validation
- Bean Validation (@NotNull, @Email, @Size, etc.)
- Custom validators for business logic
- SQL injection prevention via parameterized queries (JPA)

---

## 8. DATABASE SCHEMA

### 8.1 Key Tables
```sql
-- Core
users                  (id, email, password, fullName, role, market, createdAt)
claims                 (id, incidentId, claimantId, status, productType, market, assignedStaffId, createdAt, updatedAt)
claim_status_history   (id, claimId, oldStatus, newStatus, reason, changedBy, changedAt)

-- Assessment & Decision
assessments            (id, claimId, assessorId, findings, status, submittedAt)
decision_records       (id, claimId, decision, reason, deciderId, decisionDate)

-- Information
information_requests   (id, claimId, requestedFields, dueDate, status, createdBy, createdAt)
information_responses  (id, requestId, response, submittedAt)

-- Team & Performance
team_workload          (id, teamId, assignedClaimsCount, avgResolutionDays, lastUpdated)
performance_metrics    (id, assessorId, claimsProcessed, avgProcessingDays, approvalRate, lastUpdated)

-- Audit
audit_logs            (id, entityType, entityId, action, userId, changes, timestamp)

-- Configuration
markets               (id, name, currency, language)
product_types         (id, market, type, minLimit, maxLimit)
sla_configurations    (id, market, productType, assessmentSLADays, decisionSLADays)
```

---

## 9. DEPLOYMENT ARCHITECTURE

### 9.1 Deployment Unit
- Single executable JAR file (Spring Boot)
- Embedded Tomcat for REST API
- Embedded Kafka consumer threads

### 9.2 Infrastructure Requirements
```
Docker Container:
├── Java 17 Runtime
├── Spring Boot App (port 8080)
├── H2 Database (file-based or in-memory)
└── Kafka Client (connects to external Kafka broker)

External:
├── Kafka Broker (3+ nodes for HA)
└── PostgreSQL/MySQL (optional: external DB for prod)
```

### 9.3 Scaling Strategy
- **Horizontal Scaling**: Multiple instances behind load balancer
- **Kafka Partitioning**: By claimId for ordering per claim
- **Database Connections**: Connection pooling (HikariCP)
- **Async Processing**: Kafka consumers process events in background

---

## 10. DEVELOPMENT WORKFLOW

### 10.1 Module Naming Convention
```
com.chubb.apac.claims.modulith
├── {module}
│   ├── model          (JPA entities)
│   ├── controller     (REST endpoints)
│   ├── service        (business logic)
│   ├── repository     (JPA repositories)
│   └── event          (event classes)
└── common
    ├── config         (Spring configs)
    ├── dto            (request/response)
    ├── exception      (error handling)
    └── security       (JWT, auth)
```

### 10.2 Coding Standards
- **Language**: Java 17 (sealed classes, records for DTOs)
- **Build**: Maven
- **Code Style**: Google Java Style Guide
- **Testing**: JUnit 5, Mockito, TestContainers for Kafka
- **Documentation**: JavaDoc for public APIs, OpenAPI specs for REST

### 10.3 Git Workflow
- Branch per feature: `feature/claim-assessment-module`
- Commit messages: `feat(assessment): add decision approval endpoint`
- PR review required before merge
- Main branch = production-ready code

---

## 11. IMPLEMENTATION ROADMAP

### Phase 1: Foundation (Week 1-2)
- [ ] User Module (auth, login)
- [ ] Common Module (config, exception handling, security)
- [ ] Kafka configuration and topic creation
- [ ] Database schema and H2 setup

### Phase 2: Claim Intake (Week 3-4)
- [ ] Incident Module (report, update)
- [ ] Claim Module (create, track, assign)
- [ ] Event publishing for incidents

### Phase 3: Assessment Workflow (Week 5-6)
- [ ] Assessment Module (review, validate)
- [ ] Information Request Module (request/response)
- [ ] Decision Module (approve/reject)

### Phase 4: Team Management (Week 7)
- [ ] Workload Module (dashboard, metrics)
- [ ] Performance tracking

### Phase 5: Polish & Testing (Week 8)
- [ ] Integration tests
- [ ] Performance testing
- [ ] API documentation (Swagger/OpenAPI)
- [ ] Security review

---

## 12. FUTURE ENHANCEMENTS

### 12.1 Microservices Evolution
- Extract Notification Service (publish-subscribe)
- Extract Payment Processing Service
- Extract Analytics/Reporting Service

### 12.2 Additional Features
- Appeal/Escalation workflow
- Document uploads (separate module)
- Third-party integrations (weather APIs for weather claims, police report APIs)
- ML-based claim routing and fraud detection

### 12.3 Infrastructure Upgrades
- Replace H2 with PostgreSQL
- Add Redis for caching and session management
- Message queue resilience improvements
- Monitoring: Prometheus + Grafana, ELK stack for logs

---

## 13. GLOSSARY

| Term | Definition |
|------|-----------|
| **Claim** | Insurance claim request by a claimant |
| **Incident** | Insurable event reported by claimant |
| **Assessment** | Review and evaluation of claim by staff |
| **Decision** | Final approval or rejection of claim by manager |
| **Claimant** | Customer filing the claim |
| **Claims Staff** | Assessor who reviews claims |
| **Manager** | Approval authority for claim decisions |
| **Market** | Geographic region (SG, MY, TH, ID, PH, VN) |
| **Product Type** | Insurance type (MOTOR, PROPERTY) |
| **Modulith** | Modular monolith (single deployable with module boundaries) |

---

## 14. REFERENCES & STANDARDS

- Spring Boot 3.x Documentation
- OpenAPI 3.0 Specification
- Kafka Streams Architecture
- Microservices Patterns (Sam Newman)
- Domain-Driven Design (Eric Evans)

---

**Approval Sign-Off:**

| Role | Name | Date | Signature |
|------|------|------|-----------|
| Tech Lead | | | |
| Product Owner | | | |
| Security Lead | | | |

