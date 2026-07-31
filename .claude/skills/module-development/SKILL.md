---
name: module-development
description: Implement or modify one CMX Assessment Spring Boot modular-monolith module from architecture and persona-specific OpenAPI contracts, using SOLID design, strict module boundaries, contract reconciliation, class-level tests, and Maven verification. Use for work in user, incident, claim, assessment, inforequest, workload, config, notification, audit, kafka, or common.
---

# CMX Module Development Skill

## Purpose

Develop one CMX Assessment module completely and consistently with the approved architecture, OpenAPI contracts, package conventions, security boundaries and quality gates.

## Required Inputs

Before coding, identify:

1. Target module and requested capability.
2. Relevant section in `docs/ARCHITECTURE.md`.
3. Relevant operations and schemas in one or more of:
   - `docs/openapi-claimant.yaml`
   - `docs/openapi-staff.yaml`
   - `docs/openapi-manager.yaml`
4. Existing code and tests in the target module.
5. Existing shared types in `common`, related module APIs/events, `pom.xml`, and `src/main/resources/application.yml`.

Do not invent an endpoint, field, role, status, event, table or dependency unless the requested change requires it. Clearly identify any necessary deviation from the approved documents before implementing it.

## Sources of Truth and Conflict Order

Use this order when interpreting requirements:

1. Explicit current task or approved acceptance criteria.
2. Persona-specific OpenAPI contract for external HTTP behaviour.
3. `docs/ARCHITECTURE.md` for module responsibility, workflow, data ownership, events and security.
4. Existing shared conventions and existing code, provided they do not conflict with items 1 to 3.
5. `docs/DEVELOPER_GUIDE.md` for project commands and working conventions.

If the sources conflict, do not silently choose one. Record the conflict in the implementation summary and make the smallest contract-safe choice. Do not change an OpenAPI contract implicitly.

## Engineering Rules

### SOLID

- **Single Responsibility:** Each class has one clear responsibility. Controllers handle HTTP concerns, services coordinate use cases, repositories persist aggregates, mappers translate representations, and validators enforce focused rules.
- **Open/Closed:** Prefer extension through strategies, policies and handlers over growing conditional blocks.
- **Liskov Substitution:** Implementations must satisfy interface contracts without weaker preconditions or surprising behaviour.
- **Interface Segregation:** Create narrow, use-case-focused interfaces. Avoid large service interfaces that expose unrelated operations.
- **Dependency Inversion:** Business services depend on abstractions. Inject dependencies through constructors.

### Module Boundaries

- Keep all code under `com.chubb.apac.claims.modulith.<module>` unless it is genuinely shared infrastructure.
- Do not access another module's repository or entity directly.
- Communicate through an explicit module API, domain/application event, or approved shared contract.
- Each module owns its persistence model and business invariants.
- Do not move module-specific DTOs, enums or utilities into `common` merely for convenience.
- Avoid circular dependencies.

### API and DTO Rules

- Treat the OpenAPI operation ID, path, method, status codes, security and schemas as the REST contract.
- Never expose a JPA entity from a controller.
- Create separate request and response DTOs. Use Java 17 records where appropriate.
- Add Jakarta Bean Validation annotations matching contract constraints.
- Keep mapping explicit in a mapper or focused mapping method. Do not use entity constructors as API mappers.
- Return the project-standard response envelope only where the relevant OpenAPI contract requires it. Reconcile inconsistent envelopes before coding and document the choice.
- Use pagination and filtering exactly as specified.

### Persistence and Transactions

- Model aggregate relationships deliberately and default collections to lazy loading.
- Avoid broad cascading and entity graphs without a use-case need.
- Add database constraints and indexes for genuine invariants and frequent queries.
- Put transaction boundaries on service implementations.
- Use read-only transactions for query use cases where appropriate.
- Prevent lost updates on concurrent state changes. Use optimistic or pessimistic locking where the use case requires it.
- Do not depend on `ddl-auto: update` as a production schema strategy.

### Security and Privacy

- Enforce role checks and data ownership at both endpoint and service boundaries.
- Apply claimant ownership, staff assignment, manager authority and market restrictions to every read and write use case.
- Derive the current user from the authenticated security context, not from client-supplied identity fields.
- Never trust market, claimant ID, assessor ID or decider ID supplied in a request when identity can be derived from the token.
- Do not log passwords, tokens, secrets, claimant contact data, free-text statements or other unnecessary personal data.
- Keep the H2 console and permissive local settings development-only.

### Events and Kafka

- Publish only domain-significant events defined by the architecture or approved task.
- Include the required event metadata: event type, aggregate identifier, timestamp, source module and correlation ID.
- Use `claimId` or `incidentId` as the prescribed partition key.
- Publish external `DecisionMade` messages to `claim.decisions` only after a successful committed decision.
- Prefer a transactional outbox for reliable database-and-Kafka consistency. If the current increment does not implement an outbox, make the delivery limitation explicit.
- Make consumers idempotent, configure retry/error handling, and route unrecoverable records to a dead-letter topic.
- Do not use Kafka as an in-process substitute where a local application event or module API is the appropriate modular-monolith mechanism.

### Exceptions and Logging

- Throw domain-specific exceptions from services.
- Map exceptions centrally to the documented HTTP status and error shape.
- Include correlation IDs in logs.
- Use structured, parameterised logging.
- Do not catch an exception only to log and suppress it.

## Required Implementation Sequence

Follow every step. Do not skip directly to code generation.

### 1. Read and Scope

1. Read `docs/ARCHITECTURE.md` fully enough to understand the system, then focus on the target module and adjacent interactions.
2. Read every relevant persona OpenAPI file. Some staff and manager paths overlap, so reconcile all definitions for the targeted operation.
3. Inspect the existing package tree, shared infrastructure, related module contracts, configuration and tests.
4. Produce an internal implementation checklist containing:
   - operations/endpoints
   - request and response schemas
   - entities and invariants
   - repositories and queries
   - service use cases
   - security rules
   - state transitions
   - produced and consumed events
   - configuration
   - tests

### 2. Model the Domain and Persistence Layer

1. Create or update required enums and value objects.
2. Create entities owned by the target module.
3. Define IDs, nullability, uniqueness, relationships, indexes, audit fields and concurrency control.
4. Encode only persistence-level invariants in the entity. Keep orchestration in services.
5. Do not create entities for response-only projections or external messages.

### 3. Create API and Event Contracts

1. Create request DTOs.
2. Create response DTOs and pagination structures.
3. Create internal command/query objects where they make use cases clearer.
4. Create event payload POJOs/records for events produced or consumed by the module.
5. Add validators and mappers.
6. Verify names, types, required fields, enum values, formats and nesting against OpenAPI.

### 4. Create Repository Interfaces

1. Extend the appropriate Spring Data repository.
2. Add only queries required by identified use cases.
3. Include ownership and market boundaries in query methods/specifications where practical.
4. Use pagination for list operations.
5. Add locking for assignment or decision race conditions when required.
6. Test custom queries and constraints.

### 5. Create Service Interfaces

1. Define small interfaces around business use cases, not CRUD tables.
2. Use DTOs, commands, query objects or domain types appropriate to the application boundary.
3. Do not expose repository, JPA or HTTP implementation details.
4. Document important preconditions, outcomes and exceptions.

### 6. Implement Business Services

1. Use constructor injection.
2. Add transaction boundaries.
3. Load data with ownership, market and role restrictions.
4. Validate current state and allowed transition.
5. Apply business rules and update the aggregate.
6. Persist changes and history/audit records.
7. Publish required events with correlation metadata.
8. Return a mapped response.
9. Make retries safe for commands that may be repeated.

### 7. Create Controllers

1. Implement exactly the documented path, HTTP method, operation, media types and status code.
2. Apply `@Valid` and typed path/query parameters.
3. Apply role-level method security.
4. Delegate business logic to a service interface.
5. Do not call repositories or Kafka producers from controllers.
6. Keep controllers free of workflow and mapping logic.

### 8. Add Configuration Only When Required

1. Reuse existing configuration where possible.
2. Add typed `@ConfigurationProperties` for module settings.
3. Validate mandatory configuration at startup.
4. Do not hard-code secrets, broker addresses, topic names, timeouts or market rules.
5. Use profile-specific configuration for local/test/production differences.

### 9. Reconcile the Implementation Against the Contracts

Build a contract matrix for every implemented operation and verify:

- path and HTTP method
- operation ID or controller method intent
- authentication and authorised role
- path and query parameters
- request fields, required fields, validation and enums
- response fields, envelope and pagination
- success status code
- documented error status codes and payload
- claimant ownership and market boundary
- workflow transition
- emitted/consumed event and topic

Remove accidental extras. Add anything required but missing. Do not expose undocumented endpoints or response fields without an approved contract change.

### 10. Write Tests for Every Class Behaviour

Write the smallest valuable test at the correct layer:

- **DTO/validation tests:** required fields, formats, sizes, enums and boundary values.
- **Mapper tests:** complete and correct field mapping; null/optional behaviour.
- **Entity/domain tests:** invariants, status transitions and equality where customised.
- **Repository tests:** use `@DataJpaTest` for custom queries, constraints, ownership/market filters and locking behaviour.
- **Service unit tests:** JUnit 5 and Mockito for happy path, not found, unauthorised/forbidden, invalid state, conflict, validation failure and event publication.
- **Controller tests:** `@WebMvcTest`, MockMvc and Spring Security Test for contract shape, validation, HTTP status and role access.
- **Kafka tests:** serializer/deserializer compatibility, topic/key selection, listener success, duplicate delivery, retry and dead-letter behaviour.
- **Integration tests:** use `@SpringBootTest` and Testcontainers where real Kafka/database behaviour matters.
- **Architecture tests:** verify module dependencies and prohibit controller-to-repository or cross-module repository access. Add ArchUnit or Spring Modulith verification before claiming automated boundary enforcement.

Do not write tests that only reproduce implementation details. Test externally visible behaviour and business rules.

### 11. Run Quality Gates and Fix Failures

Run from the project root:

```bash
mvn test
mvn clean verify
```

If a test fails:

1. Read the first meaningful failure and root cause.
2. Fix production code when behaviour is wrong.
3. Fix a test only when the test contradicts the approved requirement.
4. Re-run the narrow failing test.
5. Re-run the complete verification suite.

Do not disable, ignore or weaken a failing test merely to make the build green. Do not use `-DskipTests` as a completion gate.

### 12. Final Completion Report

Provide:

1. Module and use cases implemented.
2. Files created or changed.
3. Endpoints implemented.
4. Security and market/ownership rules applied.
5. Events produced/consumed.
6. Tests added by layer.
7. Commands run and their actual results.
8. OpenAPI comparison result, including missing, extra or conflicting items.
9. Constraints, assumptions and follow-up risks.

Never claim that tests passed unless they were executed successfully.

## Definition of Done

A module change is complete only when:

- architecture and all relevant OpenAPI files were read
- code stays inside approved module boundaries
- entities, DTOs, repositories, services, controllers and required configuration are implemented
- SOLID principles and constructor injection are followed
- security, claimant ownership and market filtering are enforced
- workflow transitions and audit/history behaviour are correct
- required domain/Kafka events are implemented safely
- OpenAPI contract reconciliation reports no unexplained missing or extra behaviour
- relevant unit, slice and integration tests exist
- `mvn clean verify` succeeds
- no secrets or unnecessary personal data are logged or committed
- the final completion report is accurate

## Project-Specific Contract Watch-outs

Check these explicitly during every relevant implementation:

1. The claimant architecture lists `GET /claimants/{claimantId}/claims`, while the claimant OpenAPI defines `GET /claims`. Follow the approved external contract or record an explicit decision.
2. Staff and manager specifications both define `/staff/claims` and decision endpoints with different operation IDs and partially different schemas. Implement one compatible runtime endpoint per HTTP method/path and reconcile persona authorisation and response shape.
3. The architecture describes a standard response envelope, while several OpenAPI responses expose unwrapped DTOs or only `data` and `pagination`. Do not apply a global envelope until the contracts are normalised or an explicit compatibility decision is made.
4. `InformationRequest.requestedFields` is described as comma-separated in the architecture but as an array in OpenAPI. Keep the API as an array; persistence representation must not leak into the API.
5. The architecture says staff/managers have multiple markets, while some simplified database examples use a single `market` column. Model authorisation from `user_markets`, not only a single user field.
6. The current architecture uses the term modular monolith but also describes Kafka for cross-module communication. Prefer clear in-process module boundaries for internal interactions and Kafka for externally durable integration unless the approved design specifically requires Kafka internally.
7. `DecisionMade` contains personal and financial data. Minimise fields, restrict access, define retention, protect the topic and avoid logging the payload.
8. Claim assignment and manager decisions are concurrency-sensitive. Enforce atomic checks, locking/versioning and idempotency.

## Suggested Package Layout

```text
com.chubb.apac.claims.modulith.<module>
├── api            # optional explicit module-facing API
├── controller
├── dto
│   ├── request
│   └── response
├── event
├── mapper
├── model
├── repository
├── service
│   └── impl
├── validation
└── config          # only when the module owns configuration
```

Use the existing project layout when compatibility matters, but add `dto`, `mapper`, `validation` and `service.impl` when they improve responsibility boundaries. Avoid broad package moves unrelated to the requested feature.
