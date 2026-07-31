# AI Working Log

## CMX Assessment: Chubb APAC Claims Processing System

## 1. Purpose of This Log

This document records how AI-assisted engineering was used to design and implement the CMX Assessment backend. It captures the prompts, decisions, challenges, corrections, and review practices applied throughout development.

The objective was not to use AI as an uncontrolled code generator. The objective was to use AI as an engineering collaborator while retaining human ownership of architecture, scope, trade-offs, security, module boundaries, and final acceptance.

The application is a Java 17 and Spring Boot 3.2 modular monolith for processing motor and property claims across APAC markets. The initial release covers the core claimant, claims-staff, and manager journeys.

---

## 2. AI-Native Engineering Approach

The development process followed a structured AI-native workflow:

1. Establish the business and architectural context before generating implementation code.
2. Define API contracts and package boundaries early.
3. Create persistent project guidance so that later prompts did not rely only on chat history.
4. Develop one bounded context at a time.
5. Require each module to follow the same implementation sequence.
6. Review AI output for architectural fit, compilation compatibility, security, and over-engineering.
7. Challenge implementation choices whenever they conflicted with the modular-monolith design or the current release scope.
8. Correct shared abstractions centrally and examine their impact on dependent modules.
9. Preserve working modules while adding new functionality.
10. Keep deferred capabilities visible without prematurely implementing them.

This approach improved consistency and made AI output easier to inspect, correct, and integrate.

---

## 3. Architecture and Contract Discovery

Before service implementation began, I worked iteratively with an AI coding assistant to analyse the assessment brief and define the system structure. I did not begin by asking for controllers or entities in isolation. I first discussed the business journeys, domain boundaries, synchronous versus event-driven communication, persistence ownership, security roles, and the initial-release scope.

The resulting foundation included:

- `ARCHITECTURE.md`, describing the modular-monolith structure, bounded contexts, claim lifecycle, module responsibilities, security model, event interactions, APAC market considerations, SLA concepts, and implementation trade-offs.
- Persona-oriented OpenAPI specifications for claimant, claims-staff, and manager operations.
- An initial package structure containing the planned modules:
  - `common`
  - `user`
  - `incident`
  - `claim`
  - `inforequest`
  - `assessment`
  - `audit`
  - `kafka`
  - `notification`
  - `workload`
  - `config`
- A Maven and Spring Boot baseline with Java 17, Spring MVC, Spring Data JPA, Spring Security, H2, Kafka dependencies, JWT support, SpringDoc OpenAPI, Actuator, Resilience4j, JUnit, Mockito, and Testcontainers.

The architecture and OpenAPI specifications were treated as engineering inputs, not infallible generated artefacts. During implementation I continued to reconcile differences between the architecture and API specifications, including response envelopes, overlapping staff and manager paths, claimant claim-listing paths, market representation, and Information Request field representation.

### Representative early prompts and discussions

- Analyse the backend assessment and identify the core domain entities and bounded contexts.
- Decide whether the solution should be a modular monolith or multiple independently deployed services for the assessment scope.
- Define REST operations required by claimants, claims staff, and managers.
- Identify where domain events are useful and where synchronous in-process interactions are simpler.
- Create OpenAPI specifications for each persona.
- Establish a package structure aligned with module ownership rather than technical layers across the whole application.
- Explain the trade-offs behind the architecture so they can be defended in a technical walkthrough.

### Human review and approval

I reviewed the proposed architecture and contracts before proceeding to implementation. The decisions I accepted included:

- A modular monolith for the initial release.
- Module-owned entities and repositories.
- REST for synchronous user-facing operations.
- Domain events for meaningful workflow transitions.
- JWT-based authentication for the local assessment application.
- APAC market restrictions as an explicit authorisation concern.
- H2 for local development and assessment demonstration.
- Deferring production concerns such as database migration tooling and an external identity provider.

I also challenged suggestions that introduced infrastructure without immediate business value, particularly using Kafka for every internal module interaction in a single deployable application.

---

## 4. Persistent AI Guidance

To avoid repeatedly explaining standards in conversational prompts, I introduced project-level guidance.

### Developer guide

I requested that `DEVELOPER_GUIDE.md` record the actual technology stack and working conventions. The guide was enhanced with:

- Java 17
- Spring Boot 3.2
- Maven
- Spring MVC
- Spring Data JPA and Hibernate
- H2
- Spring Security
- JJWT
- Spring Kafka
- SpringDoc OpenAPI
- Actuator
- Resilience4j
- JUnit 5
- Mockito
- Spring Security Test
- Spring Kafka Test
- Testcontainers

This gave later AI interactions a stable reference for versions, commands, conventions, and integration expectations.

### Module-development skill

I asked for a reusable `SKILL.md` that defined the mandatory module workflow:

```text
Read ARCHITECTURE.md
    -> Read all relevant OpenAPI operations and schemas
    -> Inspect existing module and shared contracts
    -> Identify entities, invariants, security rules, events, and tests
    -> Create entities and value types
    -> Create request/response DTOs and event contracts
    -> Create repositories
    -> Create focused service interfaces
    -> Implement business services
    -> Create thin controllers
    -> Add configuration only when required
    -> Reconcile implementation against OpenAPI
    -> Write tests at the correct layer
    -> Compile and run tests
    -> Fix failures without weakening requirements
    -> Report changes, assumptions, and remaining risks
```

The skill also required:

- SOLID design principles.
- Constructor injection.
- No JPA entities in REST responses.
- No direct cross-module repository access.
- Explicit market and ownership checks.
- Service-layer transaction boundaries.
- Idempotent event handling where delivery may repeat.
- Concurrency protection for claim assignment and decisions.
- Correlation IDs.
- No secrets or unnecessary personal data in logs.
- A final contract reconciliation and Definition of Done.

This was a deliberate AI-native practice: encode engineering standards in the repository so that the assistant can repeatedly follow the same process.

---

## 5. Implementation Strategy

Development proceeded module by module in dependency order rather than generating the whole system in one pass.

### Selected sequence

1. Common
2. User
3. Incident
4. Claim
5. Information Request
6. Assessment and Decision
7. Configuration
8. Minimal Notification
9. Integration smoke coverage
10. Demo data seeding

The sequence reflected the business lifecycle:

```text
User authentication
    -> Incident reporting
    -> Claim creation and assignment
    -> Additional information when required
    -> Assessment
    -> Manager decision
    -> Notification record
```

Each module was expected to be an additive vertical slice containing the relevant domain model, DTOs, repositories, service interface, service implementation, controller, mapper, events, and tests.

---

## 6. Prompt and Decision Journal

## 6.1 Foundation and User Module

### Prompts

- Add the project technology stack to `DEVELOPER_GUIDE.md` for later reference.
- Create a reusable module-development skill based on SOLID principles.
- Read `ARCHITECTURE.md` and the relevant OpenAPI specification, then implement the User module.
- Add the missing Spring Boot application class.
- Implement registration, login, logout, profile management, staff creation, JWT authentication, and token revocation.

### Engineering strategy

The User module established authentication and identity context for every later module. Controllers obtained the current identity from the authenticated principal rather than trusting caller-supplied user IDs. Passwords were encoded, duplicate emails were rejected, and staff market assignments were included in security claims.

### Review and corrections

I did not accept the first generated code unquestioningly. I compiled it locally and reported concrete failures:

- Missing `PageableDefault` import.
- Missing `JwtException` import.
- Broken MockMvc chaining caused by unmatched parentheses.
- Malformed JSON embedded in a Java string.
- Type mismatch after moving `UserRole` and `Market` into Common.
- A missing `TokenRevocationChecker` bean.

The corrections led to stronger project rules:

- Prefer explicit `page` and `size` request parameters over hidden pagination annotations.
- Use explicit imports for Spring, Jakarta, and JJWT classes.
- Use `ObjectMapper.writeValueAsString(...)` or Java text blocks in tests, never fragile hand-escaped JSON.
- Treat changes to Common contracts as dependency-impacting changes.
- Avoid duplicate enums with identical names in different modules.
- Ensure a concrete Spring bean satisfies every constructor dependency.
- Treat generated code as a draft until it compiles in the real repository.

This feedback loop is a central AI-native engineering practice: use compiler and runtime feedback to improve both the code and the generation instructions.

---

## 6.2 Common Module

### Prompt

- Implement the Common module completely so that all other modules can depend on it, and recheck compilation risks.

### Engineering strategy

Common was constrained to genuine cross-cutting infrastructure:

- `BaseEntity`
- Shared enums
- API and pagination envelopes
- Exception types and global exception handling
- JWT properties and token parsing
- Spring Security configuration
- JSON 401 and 403 handling
- Correlation ID propagation
- Token revocation abstraction

### Questioning the approach

I challenged the consequences of moving types into Common. A shared abstraction is valuable only if all dependent modules migrate consistently. When the Common JWT API changed from accepting a User entity to accepting `JwtClaims`, the User module had to be updated as part of the same change.

A later Information Request mapping failure exposed another important rule: only `BaseEntity` may declare the inherited JPA `@Version` property. A child entity must not declare a second version field. The extra version mapping was removed after examining the innermost Hibernate exception rather than treating the first repository error as the root cause.

---

## 6.3 Incident Module

### Prompt

- Follow `SKILL.md` to implement the complete Incident module without breaking existing modules.

### Engineering strategy

The Incident module implemented the claim-entry journey:

- Report incident
- Retrieve an owned incident
- Update an editable incident
- Add incident parties
- List incident parties
- Capture claim items
- Publish `IncidentReportedEvent` and `IncidentUpdatedEvent`

Ownership was enforced using claimant-scoped repository queries. Non-owned incidents returned not found, avoiding disclosure of another claimant's data. Incident-specific enums remained inside the Incident module.

### Review decision

Internal workflow events remained Spring application events. Kafka was deliberately deferred because external broker dependency was not necessary for correctness inside the initial modular monolith.

---

## 6.4 Claim Module

### Prompt

- Follow `SKILL.md` to implement the Claim module end to end.

### Engineering strategy

The Claim module consumed the Incident event and created one claim per incident. The implementation included:

- Idempotent claim creation.
- Claimant claim listing and detail retrieval.
- Staff claims queue with filters.
- Market-scoped staff access.
- Claim assignment and unassignment.
- Claim status history.
- Assignment history.
- Pessimistic locking for concurrent assignment.

### Questioning the approach

I reviewed whether Claim should access User data to populate staff display names. The answer was no: direct access to the User repository would violate module boundaries. Display-name enrichment was left out until an explicit User module API is introduced.

I also retained the claimant OpenAPI path `GET /claims` instead of introducing the differing architecture path containing a claimant ID, because claimant identity should come from the security context.

---

## 6.5 Information Request Module

### Prompt

- Implement the Information Request module end to end and ensure existing modules do not break.

### Engineering strategy

The module implemented:

- Staff creation of information requests.
- Staff listing of requests for assigned claims.
- Claimant listing of requests for owned claims.
- Claimant response submission.
- Request and response persistence.
- `InformationRequestedEvent` and `InformationSubmittedEvent`.
- Concurrency protection during response submission.

### Boundary review

Instead of allowing Information Request to import `ClaimRepository`, I introduced a small public Claim module API. The implementation of repository access remained inside Claim.

The OpenAPI array representation of `requestedFields` was preserved. Persistence used an ordered element collection rather than leaking a comma-separated database representation into the API.

### Runtime challenge

After integration, application startup failed while creating a repository. I traced the meaningful root cause to Hibernate's message:

```text
Given property did not match declared version property
```

The root issue was a second `@Version` field in `InformationRequest`, despite `BaseEntity` already declaring one. Removing the duplicate fixed the entity hierarchy design. This reinforced the practice of reading to the deepest cause in a stack trace rather than modifying the repository named in the top-level exception.

---

## 6.6 Assessment and Decision Module

### Prompt

- Follow `SKILL.md` to implement the Assessment module end to end without breaking existing modules.

### Engineering strategy

The module implemented:

- Assessment draft creation and updates.
- Assessment submission.
- Validation results.
- Pending manager-review queue.
- Approval and rejection decisions.
- Assessment status history.
- Claim lifecycle transitions through a Claim-owned API.
- `AssessmentSubmittedEvent`, `ValidationFailedEvent`, and `DecisionMadeEvent`.

### Review and enhancement

The first design lacked product type in the cross-module Claim view and would have required hard-coding a product during validation. I rejected that shortcut. `ClaimAccessView` was enhanced to include the real `ProductType`, allowing assessment rules and decision events to use the actual claim product.

The pending-assessment view was also market-scoped. Assessment stored an immutable market and product snapshot obtained from Claim, enabling manager filtering without direct Claim repository access.

No Assessment entity declared another `@Version` field, preventing recurrence of the earlier Hibernate problem.

---

## 6.7 Initial-Release Scope Challenge

### Prompt

- From Audit, Kafka, Notification, Workload, and Configuration, identify which modules can be skipped because of time constraints.

### Decision

I reviewed business value against implementation cost and selected:

- Keep Configuration.
- Keep a minimal Notification module.
- Defer Audit.
- Defer Kafka.
- Defer Workload.

### Rationale

- Configuration avoids scattering market and product rules through application code.
- Minimal Notification completes the visible business lifecycle.
- Existing timestamps and status histories provide a basic audit trail for the assessment.
- Spring application events are sufficient for the initial single-process modular monolith.
- Workload dashboards are useful but not required for core claim processing.

This is an example of deliberate AI-assisted scope control. The goal was not to implement every planned package. The goal was to produce a coherent, demonstrable release while explaining the deferred design.

---

## 6.8 Configuration Module

### Prompt

- Implement Configuration end to end so the initial version is complete, while preserving existing modules.

### Engineering strategy

Configuration implemented:

- Active APAC market listing.
- Products by market.
- Rules by market and product.
- Internal SLA lookup.
- Product claim limits.
- Idempotent local seed data.

Entity names such as `MarketConfiguration` and `ProductConfiguration` avoided collisions with shared enums.

### Compatibility decision

Assessment already contained an assessment-specific validation-rule model. I chose not to replace it during initial-release finalisation because that would create unnecessary regression risk. The general Configuration API can become Assessment's rule source through a later adapter.

---

## 6.9 Minimal Notification Module

### Prompt

- Implement a minimal Notification module to finalise the initial release.

### Engineering strategy

The module listens for committed domain events and creates in-app notification records for:

- Information requested.
- Information submitted.
- Decision made.

It uses `@TransactionalEventListener(AFTER_COMMIT)` so rolled-back business operations do not create notifications. Notification persistence uses a separate transaction.

### Scope decision

I deliberately did not add:

- SMTP email delivery.
- SMS.
- Push notifications.
- Kafka consumers.
- Notification REST administration.
- Direct User or Claim repository access.

Templates contain only low-risk identifiers and decision status. Free-text assessment findings, information-response content, passwords, and tokens are excluded from notification logs.

---

## 6.10 Integration Confidence and Demo Readiness

### Prompts

- Write integration tests for the implemented modules.
- Implement a minimal high-level integration test class.
- Implement a demo data seeding file.

### Engineering strategy

A high-level application context test was proposed to load all repositories and seed data together. Its purpose is to catch:

- Hibernate mapping errors.
- Duplicate version mappings.
- Invalid repository-derived queries.
- Missing Spring beans.
- Circular dependencies.
- Seed failures.
- Module wiring regressions.

The demo seeder creates deterministic synthetic data for:

- A reported claim.
- A claim awaiting information.
- An approved claim with assessment, decision, and notification.
- Claimant, staff, and manager demo accounts.

The seeder is opt-in, transactional, idempotent, and uses only `example.com` identities. It is not enabled by default for production.

---

## 7. Best AI-Native Engineering Practices Demonstrated

## 7.1 Context engineering before code generation

I supplied architecture, OpenAPI contracts, package structure, `application.yml`, and `pom.xml` before asking for module code. This reduced guesswork and made implementation prompts contract-aware.

## 7.2 Repository-based instructions

I converted repeated expectations into `DEVELOPER_GUIDE.md` and `SKILL.md`. Persistent guidance is more reliable than expecting an assistant to remember every earlier conversation.

## 7.3 Specification-first development

Each module began with architecture and OpenAPI review. Entities and services were derived from use cases and invariants rather than generated as generic CRUD layers.

## 7.4 Vertical-slice implementation

Each bounded context was implemented end to end, including data model, DTOs, repositories, services, controllers, events, and tests. This enabled continuous demonstration of business value.

## 7.5 Human-in-the-loop review

I compiled and ran the application locally, reported exact compiler and runtime failures, and required targeted corrections. AI output was reviewed, not automatically accepted.

## 7.6 Root-cause debugging

I distinguished top-level framework symptoms from underlying causes. For example, a repository-creation error was traced to an invalid Hibernate version mapping in a newly added entity.

## 7.7 Feedback converted into future constraints

Errors were not treated as isolated fixes. They became project-wide rules:

- Explicit imports.
- No duplicate shared enums.
- No extra `@Version` in inherited entities.
- ObjectMapper-based JSON in tests.
- Explicit pagination parameters.
- Dependency-impact review for Common changes.
- Full regression checks after each module.

## 7.8 Module boundary discipline

Cross-module access occurs through explicit APIs or events. A module does not reach into another module's repository merely because all packages are in one deployable application.

## 7.9 Security by construction

Authenticated identities are derived from JWT claims. Claimant ownership, staff assignment, and market restrictions are enforced in services and repository queries. Client-supplied identity fields are not trusted.

## 7.10 Event use with deliberate restraint

Domain events are used for meaningful post-commit reactions. Kafka was deferred for the initial modular monolith rather than added solely because the dependency existed in the POM.

## 7.11 Concurrency awareness

Pessimistic locking and inherited optimistic versioning were used for assignment and decision-sensitive operations.

## 7.12 Privacy-conscious observability

Correlation IDs were added, while passwords, JWTs, detailed personal statements, assessment findings, decision reasons, and claimant response bodies were excluded from logs and minimal notifications.

## 7.13 Scope management

Audit, Kafka, and Workload were consciously deferred. Configuration and a minimal Notification module were retained because they completed the core release with limited additional complexity.

## 7.14 Honest verification status

Generated code was not represented as verified until compilation and tests could run in the actual repository. Where an execution environment lacked Maven, that limitation was explicitly recorded and local `mvn clean compile`, `mvn test`, and `mvn clean verify` remained the authoritative gates.

---

## 8. Examples of Challenging AI Output

The AI-assisted process included explicit disagreement and refinement rather than passive acceptance.

### Challenge: internal Kafka everywhere

**Concern:** Kafka for every module-to-module interaction would add operational coupling to a single-process application.

**Decision:** Use Spring application events for the initial release, while keeping event contracts ready for future Kafka translation.

### Challenge: direct cross-module repository access

**Concern:** Information Request and Assessment needed Claim data.

**Decision:** Add narrow APIs implemented inside Claim. Do not inject `ClaimRepository` into those modules.

### Challenge: duplicate enum representations

**Concern:** `Market` and `UserRole` existed in both User and Common, causing type mismatches.

**Decision:** Keep shared types only in Common and migrate dependent imports.

### Challenge: hard-coded product type

**Concern:** Assessment validation initially lacked product information and risked using a hard-coded value.

**Decision:** Extend the Claim projection to contain ProductType and use the actual claim value.

### Challenge: expanding the release unnecessarily

**Concern:** Audit, Kafka, and Workload would consume time without completing the core user journey.

**Decision:** Defer them and preserve clear extension points.

### Challenge: accepting generated tests without compilation review

**Concern:** Early generated tests contained import, parentheses, and JSON-string defects.

**Decision:** Correct the code and strengthen future generation rules. Use serialised DTOs in MockMvc tests and regard compilation as mandatory.

---

## 9. Initial-Release Result

The resulting initial release includes:

- Common infrastructure and security.
- User registration, authentication, logout, profile, roles, and market assignments.
- Incident reporting, updates, parties, and claim items.
- Claim creation, claimant tracking, staff queue, assignment, unassignment, and history.
- Information Request creation, listing, and claimant response.
- Assessment drafts, submission, validation, manager review, approval, and rejection.
- Market, product, SLA, and business-rule configuration.
- Minimal in-app notification templates and notification logs.
- High-level application wiring coverage.
- Synthetic demo data for walkthroughs.

Deferred modules remain documented as future improvements:

- Full Audit module.
- Kafka integration and delivery guarantees.
- Workload dashboards and allocation analytics.
- External notification delivery.
- Production database and schema migrations.
- Production identity provider and secrets management.

---

## 10. Review and Acceptance Method

The implementation was reviewed incrementally rather than through one final bulk review.

For each module, I used the following acceptance questions:

1. Does the code implement the architecture and the relevant OpenAPI operations?
2. Does it preserve already working modules?
3. Are responsibilities separated across controller, service, mapper, and repository?
4. Are JPA entities hidden from API consumers?
5. Are claimant ownership, staff assignment, role, and market checks explicit?
6. Does the module own its data and avoid another module's repository?
7. Are transactions and concurrency controls appropriate?
8. Are events emitted only after meaningful state changes?
9. Are tests serialisation-safe and compatible with Spring Boot 3 and Java 17?
10. Did compilation or application startup reveal an assumption that should become a permanent rule?
11. Is any generated feature beyond the initial-release contract?
12. Can the design and its trade-offs be explained during the technical walkthrough?

Plans and code were accepted only after this review or revised when a concern was identified.

---

## 11. Final Reflection

The strongest outcome of this work is not the volume of generated code. It is the controlled engineering process around generation.

AI accelerated architecture exploration, contract drafting, implementation scaffolding, testing ideas, and debugging. Human judgement remained responsible for:

- Selecting the architecture.
- Reviewing contracts.
- Prioritising the release.
- Rejecting unnecessary infrastructure.
- Protecting module boundaries.
- Interpreting compiler and runtime feedback.
- Approving changes only after their impact was understood.

This project demonstrates an AI-native engineering style in which specifications, persistent instructions, narrow prompts, incremental delivery, compiler feedback, design challenges, and explicit trade-offs work together. The result is not simply AI-generated software; it is an engineer-directed system developed with AI as a disciplined collaborator.
