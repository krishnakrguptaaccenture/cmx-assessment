# Developer Quick Reference

## Technology Stack

This section is the authoritative quick reference for the technologies currently used by the CMX Assessment backend. Keep it aligned with `pom.xml` and `application.yml` whenever a dependency, runtime, or infrastructure component changes.

### Core Platform

| Area | Technology | Project Version / Configuration | Purpose |
|---|---|---|---|
| Language | Java | 17 | Application language; DTOs may use records and Java 17 language features |
| Framework | Spring Boot | 3.2.0 | Application bootstrap, auto-configuration, dependency management and executable JAR packaging |
| Build | Apache Maven | Maven project | Dependency management, compilation, testing and packaging |
| Web | Spring MVC / Embedded Tomcat | `spring-boot-starter-web` | REST APIs on port `8080` |
| Validation | Jakarta Bean Validation | `spring-boot-starter-validation` | Request and domain input validation |
| Persistence | Spring Data JPA / Hibernate | Managed by Spring Boot 3.2.0 | ORM, repositories and transactional persistence |
| Development Database | H2 | Runtime dependency; in-memory `chubbdb` | Local development and automated tests |
| Messaging | Apache Kafka / Spring for Apache Kafka | Kafka client 3.6.0 | Domain event publication and consumption |
| Security | Spring Security | Managed by Spring Boot 3.2.0 | Authentication, authorisation, endpoint and method security |
| Token Support | JJWT | 0.12.3 | JWT creation, parsing and validation |
| API Documentation | SpringDoc OpenAPI | 2.2.0 | OpenAPI generation and Swagger UI |
| Resilience | Resilience4j | 2.1.0 | Circuit breaker and resilience policies when external integrations are introduced |
| Boilerplate Reduction | Lombok | Managed by Spring Boot | Compile-time generation of common Java code |
| Observability | Spring Boot Actuator | Configured endpoints | Health, info, metrics and Prometheus endpoints |

### Testing Stack

| Area | Technology | Project Version / Scope | Purpose |
|---|---|---|---|
| Unit and integration tests | JUnit 5 | Via `spring-boot-starter-test` | Test execution and assertions |
| Mocking | Mockito | Via `spring-boot-starter-test` | Isolated unit testing |
| Spring integration testing | Spring Test | Via `spring-boot-starter-test` | Application-context and web-layer tests |
| Security testing | Spring Security Test | Test scope | Authentication and authorisation test support |
| Kafka testing | Spring Kafka Test | Test scope | Kafka producer/consumer test utilities |
| Containerised integration tests | Testcontainers | 1.19.3 | Reproducible infrastructure integration tests |
| Kafka containers | Testcontainers Kafka | 1.19.3 | Kafka integration testing |

### Runtime Configuration Reference

- Application name: `cmx-assessment`
- API base prefix: `/api/v1`
- HTTP port: `8080`
- Development database URL: `jdbc:h2:mem:chubbdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false`
- H2 console: `/h2-console`, development only
- Kafka bootstrap server: `localhost:9092`
- Kafka consumer group: `cmx-claims-group`
- JWT expiry: `86400000` milliseconds, configurable
- OpenAPI JSON: `/v3/api-docs`
- Swagger UI: `/swagger-ui.html`
- Actuator exposure: `health`, `info`, `metrics`, `prometheus`

### Planned Production Evolution

The architecture allows the following later changes, but they are not part of the current local baseline:

- Replace H2 with PostgreSQL or MySQL.
- Manage schema changes with Flyway or Liquibase rather than `ddl-auto: update`.
- Supply JWT and Kafka secrets through an approved secrets-management mechanism.
- Deploy Kafka as an external highly available cluster.
- Add Prometheus/Grafana and centralised logging.

## Project Setup

### First Time Setup

```bash
cd C:\Users\krishna.j.gupta\IdeaProjects\cmx-assessment
mvn clean install
docker-compose up -d kafka
mvn spring-boot:run
```

### IDE Setup (IntelliJ IDEA)

1. Open the project directory.
2. Mark `src/main/java` as Sources Root.
3. Mark `src/test/java` as Test Sources Root.
4. Set the project SDK to Java 17.
5. Enable annotation processing.

## Module Structure at a Glance

| Module | Purpose | REST Prefix |
|---|---|---|
| `user` | Authentication, authorisation and profiles | `/api/v1/auth`, `/api/v1/users` |
| `incident` | Incident reporting and parties | `/api/v1/incidents` |
| `claim` | Claim orchestration and lifecycle | `/api/v1/claims`, `/api/v1/staff/claims` |
| `assessment` | Assessment, validation and decisions | `/api/v1/staff/assessments`, `/api/v1/staff/decisions` |
| `inforequest` | Additional-information workflow | Claim-scoped claimant and staff endpoints |
| `workload` | Workload, SLA and performance dashboards | `/api/v1/staff/dashboard` |
| `config` | Market, product, SLA and business-rule configuration | `/api/v1/config` |
| `notification` | Notification templates and logging | No public REST API |
| `audit` | Immutable compliance trail | No public REST API |
| `kafka` | Kafka configuration, producers and consumers | No public REST API |
| `common` | Shared security, DTO, exception and utility infrastructure | Cross-cutting |

## Mandatory Module Development Workflow

Use `.claude/skills/module-development/SKILL.md` whenever implementing or changing a module. The skill requires architecture-first and OpenAPI-first development, SOLID design, contract reconciliation, class-level tests and a final `mvn clean verify` quality gate.

## Coding Standards

- Follow Java 17 and Spring Boot 3 conventions.
- Follow SOLID principles and keep controllers thin.
- Constructor-inject dependencies. Do not use field injection.
- Put business logic behind service interfaces.
- Do not expose JPA entities directly from REST endpoints.
- Use request and response DTOs at API boundaries.
- Apply transactions in the service layer.
- Enforce role, claimant ownership and market boundaries in the service layer, not only in controllers.
- Use domain-specific exceptions mapped by the global exception handler.
- Keep module internals private; expose only purposeful APIs and events.
- Use pagination for collection endpoints.
- Never log passwords, JWTs, secrets or unnecessary personal data.
- Use the OpenAPI specification as the external contract and `ARCHITECTURE.md` as the structural and domain source of truth.

## Common Maven Commands

```bash
mvn clean install
mvn test
mvn verify
mvn clean install -DskipTests
mvn test -Dtest=ClaimServiceTest
mvn package
mvn dependency:tree
mvn versions:display-dependency-updates
```

## Local URLs

- Application: `http://localhost:8080`
- H2 console: `http://localhost:8080/h2-console`
- OpenAPI document: `http://localhost:8080/v3/api-docs`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- Actuator health: `http://localhost:8080/actuator/health`

## Documentation

- Architecture: `docs/ARCHITECTURE.md`
- Claimant API: `docs/openapi-claimant.yaml`
- Staff API: `docs/openapi-staff.yaml`
- Manager API: `docs/openapi-manager.yaml`
- Module development skill: `.claude/skills/module-development/SKILL.md`
- This guide: `docs/DEVELOPER_GUIDE.md`
