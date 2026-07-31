# Developer Quick Reference

## Project Setup

### First Time Setup
```bash
# Navigate to project
cd C:\Users\krishna.j.gupta\IdeaProjects\cmx-assessment

# Build project
mvn clean install

# Start Kafka (Docker required)
docker-compose up -d kafka

# Run application
mvn spring-boot:run
```

### IDE Setup (IntelliJ IDEA)
1. File → Open → Select project directory
2. Mark `src/main/java` as Sources Root
3. Mark `src/test/java` as Test Sources Root
4. File → Project Structure → Set SDK to Java 17
5. Enable annotation processing: Settings → Compiler → Annotation Processors → Enable

## Module Structure at a Glance

| Module | Purpose | Key Classes | REST Prefix |
|--------|---------|------------|-------------|
| **user** | Auth & profiles | User, UserRole, AuthController | /api/v1/auth |
| **incident** | Incident reporting | Incident, IncidentParty, IncidentController | /api/v1/incidents |
| **claim** | Claim orchestration | Claim, ClaimStatus, ClaimController | /api/v1/claims |
| **assessment** | Claims review | Assessment, DecisionRecord, AssessmentController | /api/v1/staff/assessments |
| **inforequest** | Info requests | InformationRequest, InfoRequestController | /api/v1/info-requests |
| **workload** | Performance tracking | TeamWorkload, PerformanceMetric, DashboardController | /api/v1/staff/dashboard |
| **config** | Market & rules | Market, ProductType, ConfigController | /api/v1/config |
| **notification** | Templates | NotificationService | (no REST) |
| **audit** | Compliance | AuditLog, AuditService | (no REST) |

## Coding Patterns

### Creating a New REST Endpoint

**1. Create Entity** (`model/Claim.java`):
```java
@Entity
@Table(name = "claims")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Claim extends BaseEntity {
    private String claimId;
    private String incidentId;
    private String claimantId;
    private ClaimStatus status;
    
    @ManyToOne
    @JoinColumn(name = "assigned_staff_id")
    private User assignedStaff;
}
```

**2. Create Repository** (`repository/ClaimRepository.java`):
```java
public interface ClaimRepository extends JpaRepository<Claim, String> {
    List<Claim> findByClaimantId(String claimantId);
    List<Claim> findByStatus(ClaimStatus status);
    Optional<Claim> findByClaimId(String claimId);
}
```

**3. Create Service** (`service/ClaimService.java`):
```java
@Service
@Slf4j
public class ClaimService {
    @Autowired
    private ClaimRepository claimRepository;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    public ClaimResponse getClaim(String claimId) {
        Claim claim = claimRepository.findByClaimId(claimId)
            .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));
        return mapToResponse(claim);
    }
    
    public ClaimResponse createClaim(CreateClaimRequest request) {
        Claim claim = new Claim();
        claim.setClaimId(generateClaimId());
        claim.setStatus(ClaimStatus.REPORTED);
        claimRepository.save(claim);
        
        // Publish event
        eventPublisher.publishEvent(new ClaimCreatedEvent(claim));
        
        return mapToResponse(claim);
    }
}
```

**4. Create Controller** (`controller/ClaimController.java`):
```java
@RestController
@RequestMapping("/api/v1/claims")
@Slf4j
public class ClaimController {
    @Autowired
    private ClaimService claimService;
    
    @GetMapping("/{claimId}")
    public ResponseEntity<ApiResponse<ClaimResponse>> getClaim(
        @PathVariable String claimId,
        @AuthenticationPrincipal UserDetails userDetails) {
        ClaimResponse claim = claimService.getClaim(claimId);
        return ResponseEntity.ok(ApiResponse.success(claim));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<ClaimResponse>> createClaim(
        @Valid @RequestBody CreateClaimRequest request,
        @AuthenticationPrincipal UserDetails userDetails) {
        ClaimResponse claim = claimService.createClaim(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(claim));
    }
}
```

### Publishing Events

```java
// In service layer
eventPublisher.publishEvent(new ClaimStatusChangedEvent(
    claimId, 
    oldStatus, 
    newStatus, 
    "Assessment completed", 
    userId
));
```

### Consuming Events (Kafka)

```java
@Component
@Slf4j
public class AssessmentEventListener {
    
    @KafkaListener(topics = "assessment.events", groupId = "cmx-claims-group")
    public void onAssessmentSubmitted(AssessmentSubmittedEvent event) {
        log.info("Assessment submitted for claim: {}", event.getClaimId());
        // Handle event
    }
}
```

## Common Classes & Utilities

### BaseEntity (All entities inherit)
```java
@MappedSuperclass
@Data
public abstract class BaseEntity {
    @Id
    private String id;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    private String createdBy;
}
```

### ApiResponse (Standard REST response)
```java
@Data
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private ErrorDetail error;
    private LocalDateTime timestamp;
    
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.data = data;
        response.timestamp = LocalDateTime.now();
        return response;
    }
}
```

### Exceptions

```java
// Use these in service layer
throw new ResourceNotFoundException("Claim not found");
throw new UnauthorizedException("Not authorized to view this claim");
throw new ValidationException("Invalid assessment submission");
throw new ConflictException("Claim already assigned");
```

## Testing Guidelines

### Unit Test Template
```java
@ExtendWith(MockitoExtension.class)
class ClaimServiceTest {
    
    @Mock
    private ClaimRepository claimRepository;
    
    @InjectMocks
    private ClaimService claimService;
    
    @Test
    void testGetClaim_Success() {
        // Arrange
        Claim claim = new Claim();
        claim.setClaimId("CLM-001");
        when(claimRepository.findByClaimId("CLM-001")).thenReturn(Optional.of(claim));
        
        // Act
        ClaimResponse response = claimService.getClaim("CLM-001");
        
        // Assert
        assertNotNull(response);
        assertEquals("CLM-001", response.getClaimId());
    }
}
```

### Integration Test
```java
@SpringBootTest
class ClaimControllerIntTest {
    
    @Autowired
    private WebTestClient webTestClient;
    
    @Test
    void testCreateClaim() {
        webTestClient
            .post()
            .uri("/api/v1/claims")
            .header("Authorization", "Bearer " + getValidToken())
            .bodyValue(new CreateClaimRequest())
            .exchange()
            .expectStatus().isCreated()
            .expectBody(ApiResponse.class);
    }
}
```

## Database Queries (H2 Console)

Access at: http://localhost:8080/h2-console

**Useful queries:**
```sql
-- List all claims
SELECT * FROM claims ORDER BY created_at DESC;

-- Claims by status
SELECT * FROM claims WHERE status = 'UNDER_REVIEW';

-- Claims assigned to user
SELECT * FROM claims WHERE assigned_staff_id = 'STAFF001';

-- Audit trail for a claim
SELECT * FROM audit_logs WHERE entity_id = 'CLM-001' ORDER BY timestamp DESC;
```

## Git Workflow

```bash
# Create feature branch
git checkout -b feature/claim-assessment-module

# Commit with meaningful message
git commit -m "feat(assessment): add decision approval endpoint"

# Push branch
git push origin feature/claim-assessment-module

# Create Pull Request on GitHub
# Get review approval
# Merge to main
```

## Troubleshooting

### Kafka Connection Issues
```bash
# Check if Kafka is running
docker ps | grep kafka

# View logs
docker logs kafka

# Restart Kafka
docker-compose down && docker-compose up -d kafka
```

### Port Already in Use
```bash
# Kill process on port 8080
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Or change port in application.yml
server:
  port: 8081
```

### Database Lock
```bash
# H2 in-memory database sometimes locks
# Restart the application or:
# DELETE FROM claims;  -- Clear if needed
```

## Useful Maven Commands

```bash
# Clean build
mvn clean install

# Run only tests
mvn test

# Run integration tests
mvn verify

# Skip tests during build
mvn clean install -DskipTests

# Run specific test
mvn test -Dtest=ClaimServiceTest

# Build JAR
mvn package

# Show dependency tree
mvn dependency:tree

# Check for outdated dependencies
mvn versions:display-dependency-updates
```

## API Testing

### Using cURL
```bash
# Login
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password"}'

# Get claims (with token)
curl -X GET http://localhost:8080/api/v1/claims \
  -H "Authorization: Bearer <token>"
```

### Using Postman
1. Import OpenAPI specs into Postman
2. Set `{{base_url}}` = http://localhost:8080
3. Set `{{token}}` from login response
4. Use in Authorization header: `Bearer {{token}}`

## Performance Tips

- Use pagination for list endpoints (default: 10 items per page)
- Add indexes on frequently queried fields:
  ```sql
  CREATE INDEX idx_claims_claimant ON claims(claimant_id);
  CREATE INDEX idx_claims_status ON claims(status);
  ```
- Cache market/config data (rarely changes)
- Use async processing for heavy operations (Kafka)

## Documentation

- **Architecture**: `docs/ARCHITECTURE.md`
- **API Claimant**: `docs/openapi-claimant.yaml`
- **API Staff**: `docs/openapi-staff.yaml`
- **API Manager**: `docs/openapi-manager.yaml`
- **This Guide**: `docs/DEVELOPER_GUIDE.md`

---

**Happy Coding! 🚀**
