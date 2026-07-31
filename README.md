# CMX Assessment - Chubb APAC Claims Processing System

## Project Overview

CMX Assessment is a **modular monolith** backend application built with Spring Boot 3.x on Java 17 that processes motor and property insurance claims across six APAC markets (Singapore, Malaysia, Thailand, Indonesia, Philippines, Vietnam).

The system provides a clear separation between three user personas:
- **Claimants**: Report incidents and track their claims
- **Claims Staff**: Review, assess, and manage claims
- **Managers**: Oversee team performance and approve/reject claim decisions

## Key Features

✅ **Incident Reporting** - Claimants can report incidents with full party and item details
✅ **Claim Lifecycle Management** - Track claims from reported → under review → decision
✅ **Assessment Workflow** - Staff review claims, validate against business rules
✅ **Decision Making** - Managers approve/reject claims; decisions published to Kafka
✅ **Information Requests** - Staff can request additional info from claimants
✅ **Team Workload Dashboard** - Real-time view of team assignments and performance
✅ **Multi-Market Support** - Isolated configuration per market with local business rules
✅ **Event-Driven Architecture** - Kafka integration for async notifications and downstream processing
✅ **Complete Audit Trail** - All claim actions logged for compliance

## Architecture

This is a **modular monolith** - a single deployable application with clear module boundaries:

```
cmx-assessment/
├── user/              (Authentication & user management)
├── incident/          (Incident reporting & parties)
├── claim/             (Central claim orchestrator)
├── assessment/        (Claims review & decision)
├── inforequest/       (Information request/response)
├── workload/          (Team performance & SLA tracking)
├── config/            (Market & business rules)
├── notification/      (Notification templates - external service consumes)
├── audit/             (Compliance logging)
├── kafka/             (Event infrastructure)
└── common/            (Shared utilities, security, config)
```

**Key Design Decisions:**

- **Single Database** - All modules use shared H2 database (no distributed transactions)
- **REST + Kafka Hybrid** - REST for sync client interactions, Kafka for async workflows
- **Event Sourcing Ready** - Audit trail enables compliance and debugging
- **Market-Level Multi-Tenancy** - Data filtered by market for security and isolation
- **Future-Proof** - Modules can be extracted to microservices without major refactoring

## Technology Stack

| Component | Technology |
|-----------|-----------|
| **Language** | Java 17 (LTS) |
| **Framework** | Spring Boot 3.x |
| **Database** | H2 (in-memory/file-based) |
| **Message Queue** | Apache Kafka |
| **API** | REST (Spring Web MVC) |
| **Security** | JWT Bearer Tokens |
| **Documentation** | OpenAPI 3.0 (Swagger/Springdoc) |
| **Build** | Maven |
| **Testing** | JUnit 5, Mockito, TestContainers |

## Project Structure

```
cmx-assessment/
├── src/
│   ├── main/
│   │   ├── java/com/chubb/apac/claims/modulith/
│   │   │   ├── {module}/
│   │   │   │   ├── model/          (JPA entities)
│   │   │   │   ├── controller/     (REST endpoints)
│   │   │   │   ├── service/        (Business logic)
│   │   │   │   ├── repository/     (JPA repositories)
│   │   │   │   └── event/          (Event classes)
│   │   │   └── common/
│   │   │       ├── config/         (Spring configurations)
│   │   │       ├── dto/            (Shared DTOs)
│   │   │       ├── exception/      (Global exception handling)
│   │   │       └── security/       (JWT utilities)
│   │   └── resources/
│   │       └── application.yml     (Configuration)
│   └── test/
│       └── java/com/chubb/apac/claims/modulith/
│           └── {module}/           (Unit & integration tests)
├── docs/
│   ├── ARCHITECTURE.md             (Detailed architecture)
│   ├── openapi-claimant.yaml       (Claimant API spec)
│   ├── openapi-staff.yaml          (Staff API spec)
│   ├── openapi-manager.yaml        (Manager API spec)
│   └── API-CONVENTIONS.md          (API design standards)
├── pom.xml                         (Maven dependencies)
└── README.md                       (This file)
```

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- Docker (optional, for Kafka)
- Git

### Installation

1. **Clone the repository:**
   ```bash
   cd C:\Users\krishna.j.gupta\IdeaProjects\cmx-assessment
   ```

2. **Install dependencies:**
   ```bash
   mvn clean install
   ```

3. **Start Kafka (Docker):**
   ```bash
   docker-compose up -d
   ```

4. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

   Application starts on `http://localhost:8080`

### Access Points

| Service | URL | Purpose |
|---------|-----|---------|
| **Swagger UI** | http://localhost:8080/swagger-ui.html | API documentation |
| **API Docs JSON** | http://localhost:8080/v3/api-docs | OpenAPI schema |
| **Health Check** | http://localhost:8080/actuator/health | Application health |
| **H2 Console** | http://localhost:8080/h2-console | Database console |

## API Documentation

Comprehensive OpenAPI specifications are provided:

### Claimant API (`/api/v1/`)
- Report incidents
- Track claims
- Submit additional information
- View claim status

📄 **Spec:** `docs/openapi-claimant.yaml`

### Staff API (`/api/v1/staff/`)
- View all claims (filtered)
- Assign claims to self
- Submit assessments
- Request information from claimants
- Validate assessments

📄 **Spec:** `docs/openapi-staff.yaml`

### Manager API (`/api/v1/staff/dashboard/`)
- Real-time workload overview
- Staff performance metrics
- SLA compliance tracking
- Approve/reject claim decisions

📄 **Spec:** `docs/openapi-manager.yaml`

## Authentication

All endpoints (except `/auth/register` and `/auth/login`) require JWT Bearer token:

```bash
Authorization: Bearer <jwt_token>
```

**Token Claims:**
- `userId` - User identifier
- `email` - User email
- `role` - User role (CLAIMANT, CLAIMS_STAFF, MANAGER)
- `market` - Assigned market (for staff/managers)
- `exp` - Expiration time

**Token Expiration:** 24 hours (configurable via `application.yml`)

## Kafka Topics

Events are published to Kafka for downstream services to consume:

| Topic | Events | Purpose |
|-------|--------|---------|
| `incident.events` | IncidentReported, IncidentUpdated | Trigger claim creation |
| `claim.events` | ClaimCreated, ClaimStatusChanged | Update workload/metrics |
| `inforequest.events` | InformationRequested, InformationSubmitted | Notify claimant/staff |
| `assessment.events` | AssessmentSubmitted | Trigger manager review |
| `claim.decisions` | **APPROVED / REJECTED** | External services (notifications, payments) |

**Key Decision Event Payload:**
```json
{
  "eventType": "DecisionMade",
  "claimId": "CLM-2026-000123",
  "decision": "APPROVED",
  "decisionReason": "All documents verified, within policy limits",
  "deciderId": "MGR001",
  "decisionDate": "2026-07-31T13:35:11Z",
  "claimantEmail": "john@example.com",
  "market": "SG",
  "productType": "MOTOR"
}
```

## Configuration

Environment variables and configuration in `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:chubbdb
  kafka:
    bootstrap-servers: localhost:9092
  security:
    jwt:
      secret: ${JWT_SECRET}
      expiration: 86400000  # 24 hours

server:
  port: 8080
```

**Override in production:**
```bash
export JWT_SECRET=your-secret-key-min-256-bits
export SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka-broker:9092
```

## Development

### Running Tests

```bash
# Unit tests
mvn test

# Integration tests
mvn verify

# Specific test
mvn test -Dtest=ClaimControllerTest
```

### Code Style

Follow **Google Java Style Guide**:
- 4-space indentation
- Max line length: 120 characters
- Use meaningful variable names
- Avoid deep nesting

### Module Development Checklist

- [ ] Create entity classes in `model/`
- [ ] Create REST controller in `controller/`
- [ ] Implement service logic in `service/`
- [ ] Create JPA repository in `repository/`
- [ ] Define events in `event/` (if publishing events)
- [ ] Add unit tests
- [ ] Update OpenAPI spec
- [ ] Add audit logging
- [ ] Document API endpoints

## Database Schema

Key tables:

```sql
users              -- User accounts and roles
claims             -- Main claim records
claim_status_history -- Audit trail for status changes
incidents          -- Incident reports
assessments        -- Claim assessments
decision_records   -- Final approval/rejection decisions
information_requests -- Info requests from staff
team_workload      -- Team performance metrics
performance_metrics -- Individual assessor KPIs
audit_logs         -- Complete audit trail
markets            -- Supported markets
sla_configurations -- Market-specific SLA timelines
```

## Deployment

### Local Development
```bash
mvn spring-boot:run
```

### Docker Build
```bash
mvn clean package
docker build -t cmx-assessment:1.0.0 .
docker run -p 8080:8080 -e JWT_SECRET=... cmx-assessment:1.0.0
```

### Production Checklist
- [ ] Use external PostgreSQL database
- [ ] Enable HTTPS/TLS
- [ ] Configure production JWT secret
- [ ] Set up Kafka cluster with replication
- [ ] Enable monitoring (Prometheus, Grafana)
- [ ] Configure logging (ELK stack)
- [ ] Set up backup strategy
- [ ] Perform security audit

## Roadmap

### Phase 1: Foundation (Done)
- ✅ Architecture design
- ✅ Spring Boot setup
- ✅ OpenAPI specifications

### Phase 2: Implementation
- [ ] User authentication & authorization
- [ ] Incident & Claim modules
- [ ] Assessment workflow
- [ ] Decision approval process
- [ ] Information request module
- [ ] Workload dashboard
- [ ] Integration tests
- [ ] API documentation

### Phase 3: Enhancement
- [ ] Appeal workflow
- [ ] Advanced reporting & analytics
- [ ] Machine learning for claim routing
- [ ] Third-party integrations
- [ ] Redis caching layer

## Support & Contributing

For issues, questions, or contributions:

1. **Create an issue** in the repository
2. **Follow code style** guidelines
3. **Add tests** for new features
4. **Update documentation** accordingly
5. **Submit pull request** for review

## License

Proprietary - Chubb APAC

## Contact

**Team:** Claims Modernization (CMX)  
**Email:** cmx-team@chubb-apac.com  
**Slack:** #cmx-assessment-dev

---

**Last Updated:** 2026-07-31  
**Version:** 1.0.0
