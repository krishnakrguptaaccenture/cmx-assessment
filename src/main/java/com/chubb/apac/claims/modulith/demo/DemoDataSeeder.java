package com.chubb.apac.claims.modulith.demo;

import com.chubb.apac.claims.modulith.assessment.model.Assessment;
import com.chubb.apac.claims.modulith.assessment.model.AssessmentStatus;
import com.chubb.apac.claims.modulith.assessment.model.DecisionRecord;
import com.chubb.apac.claims.modulith.assessment.model.RecommendedDecision;
import com.chubb.apac.claims.modulith.assessment.repository.AssessmentRepository;
import com.chubb.apac.claims.modulith.assessment.repository.DecisionRecordRepository;
import com.chubb.apac.claims.modulith.claim.model.Claim;
import com.chubb.apac.claims.modulith.claim.model.ClaimAssignment;
import com.chubb.apac.claims.modulith.claim.model.ClaimStatusHistory;
import com.chubb.apac.claims.modulith.claim.repository.ClaimAssignmentRepository;
import com.chubb.apac.claims.modulith.claim.repository.ClaimRepository;
import com.chubb.apac.claims.modulith.claim.repository.ClaimStatusHistoryRepository;
import com.chubb.apac.claims.modulith.common.enums.ClaimStatus;
import com.chubb.apac.claims.modulith.common.enums.Decision;
import com.chubb.apac.claims.modulith.common.enums.Market;
import com.chubb.apac.claims.modulith.common.enums.ProductType;
import com.chubb.apac.claims.modulith.common.enums.UserRole;
import com.chubb.apac.claims.modulith.incident.model.ClaimItem;
import com.chubb.apac.claims.modulith.incident.model.ClaimItemType;
import com.chubb.apac.claims.modulith.incident.model.Incident;
import com.chubb.apac.claims.modulith.incident.model.IncidentParty;
import com.chubb.apac.claims.modulith.incident.model.IncidentPartyType;
import com.chubb.apac.claims.modulith.incident.model.IncidentStatus;
import com.chubb.apac.claims.modulith.incident.model.IncidentType;
import com.chubb.apac.claims.modulith.incident.repository.IncidentRepository;
import com.chubb.apac.claims.modulith.inforequest.model.InformationRequest;
import com.chubb.apac.claims.modulith.inforequest.model.InformationRequestStatus;
import com.chubb.apac.claims.modulith.inforequest.repository.InformationRequestRepository;
import com.chubb.apac.claims.modulith.notification.model.NotificationChannel;
import com.chubb.apac.claims.modulith.notification.model.NotificationEventType;
import com.chubb.apac.claims.modulith.notification.model.NotificationLog;
import com.chubb.apac.claims.modulith.notification.model.NotificationStatus;
import com.chubb.apac.claims.modulith.notification.repository.NotificationLogRepository;
import com.chubb.apac.claims.modulith.user.model.User;
import com.chubb.apac.claims.modulith.user.repository.UserRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Seeds deterministic, synthetic demonstration data for the CMX initial release.
 *
 * <p>The seeder is disabled unless {@code cmx.demo.seed-enabled=true}. All identities use
 * example.com addresses and must never be enabled in production. Configuration and notification
 * template reference data remain owned by their existing module seeders.</p>
 */
@Component
@Order(200)
@ConditionalOnProperty(name = "cmx.demo.seed-enabled", havingValue = "true")
public class DemoDataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoDataSeeder.class);
    private static final String SYSTEM = "demo-seeder";
    private static final String DEMO_PASSWORD = "DemoPass123!";

    private final UserRepository users;
    private final IncidentRepository incidents;
    private final ClaimRepository claims;
    private final ClaimStatusHistoryRepository claimHistory;
    private final ClaimAssignmentRepository assignments;
    private final InformationRequestRepository informationRequests;
    private final AssessmentRepository assessments;
    private final DecisionRecordRepository decisions;
    private final NotificationLogRepository notifications;
    private final PasswordEncoder passwordEncoder;

    public DemoDataSeeder(
            UserRepository users,
            IncidentRepository incidents,
            ClaimRepository claims,
            ClaimStatusHistoryRepository claimHistory,
            ClaimAssignmentRepository assignments,
            InformationRequestRepository informationRequests,
            AssessmentRepository assessments,
            DecisionRecordRepository decisions,
            NotificationLogRepository notifications,
            PasswordEncoder passwordEncoder) {
        this.users = users;
        this.incidents = incidents;
        this.claims = claims;
        this.claimHistory = claimHistory;
        this.assignments = assignments;
        this.informationRequests = informationRequests;
        this.assessments = assessments;
        this.decisions = decisions;
        this.notifications = notifications;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (users.existsByEmailIgnoreCase("claimant.demo@example.com")) {
            log.info("CMX demo data already exists; seeding skipped");
            return;
        }

        User claimant = createUser(
                "USR-DEMO-CLAIMANT", "claimant.demo@example.com", "Demo Claimant",
                UserRole.CLAIMANT, null);
        User staff = createUser(
                "USR-DEMO-STAFF", "staff.demo@example.com", "Demo Claims Staff",
                UserRole.CLAIMS_STAFF, "TEAM-SG-01");
        User manager = createUser(
                "USR-DEMO-MANAGER", "manager.demo@example.com", "Demo Manager",
                UserRole.MANAGER, "TEAM-SG-01");
        users.saveAll(List.of(claimant, staff, manager));

        seedReportedClaim(claimant);
        seedAwaitingInformationClaim(claimant, staff);
        seedApprovedClaim(claimant, staff, manager);

        log.info(
                "CMX synthetic demo data seeded: claimant={}, staff={}, manager={}",
                claimant.getEmail(), staff.getEmail(), manager.getEmail());
    }

    private User createUser(
            String id, String email, String fullName, UserRole role, String teamId) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(DEMO_PASSWORD));
        user.setFullName(fullName);
        user.setPhoneNumber("+65-0000-0000");
        user.setActive(true);
        user.getRoles().add(role);
        if (role != UserRole.CLAIMANT) {
            user.getMarkets().add(Market.SG);
        }
        user.setTeamId(teamId);
        return user;
    }

    private void seedReportedClaim(User claimant) {
        Instant now = Instant.now();
        Incident incident = baseIncident(
                "INC-DEMO-001", claimant.getId(), IncidentType.MOTOR_ACCIDENT,
                "Orchard Road, Singapore", "Synthetic minor vehicle collision", now.minusSeconds(86_400));
        incidents.save(incident);

        Claim claim = baseClaim(
                "CLM-DEMO-001", incident.getIncidentId(), claimant.getId(),
                ProductType.MOTOR, ClaimStatus.REPORTED, null);
        claims.save(claim);
        claimHistory.save(history(
                claim.getClaimId(), null, ClaimStatus.REPORTED,
                "Claim created from demo incident", SYSTEM, now.minusSeconds(86_300)));
    }

    private void seedAwaitingInformationClaim(User claimant, User staff) {
        Instant now = Instant.now();
        Incident incident = baseIncident(
                "INC-DEMO-002", claimant.getId(), IncidentType.PROPERTY_DAMAGE,
                "Singapore", "Synthetic water damage to insured property", now.minusSeconds(172_800));
        incidents.save(incident);

        Claim claim = baseClaim(
                "CLM-DEMO-002", incident.getIncidentId(), claimant.getId(),
                ProductType.PROPERTY, ClaimStatus.AWAITING_INFORMATION, staff.getId());
        claims.save(claim);
        claimHistory.save(history(
                claim.getClaimId(), ClaimStatus.UNDER_REVIEW, ClaimStatus.AWAITING_INFORMATION,
                "Additional documents requested", staff.getId(), now.minusSeconds(43_200)));

        ClaimAssignment assignment = new ClaimAssignment();
        assignment.setClaimId(claim.getClaimId());
        assignment.setStaffId(staff.getId());
        assignment.setAssignedAt(now.minusSeconds(86_400));
        audit(assignment, staff.getId());
        assignments.save(assignment);

        InformationRequest request = new InformationRequest();
        request.setRequestId("IRQ-DEMO-001");
        request.setClaimId(claim.getClaimId());
        request.setRequestedFields(List.of("Repair quotation", "Damage photographs"));
        request.setDueDate(LocalDate.now().plusDays(5));
        request.setStatus(InformationRequestStatus.PENDING);
        request.setInstructions("Upload synthetic supporting documents for the demo claim.");
        request.setRequestedBy(staff.getId());
        audit(request, staff.getId());
        informationRequests.save(request);

        notifications.save(notification(
                "NTF-DEMO-001", NotificationEventType.INFORMATION_REQUESTED,
                claim.getClaimId(), "Additional information requested for claim CLM-DEMO-002",
                "Information request IRQ-DEMO-001 is awaiting a response.", now.minusSeconds(43_100)));
    }

    private void seedApprovedClaim(User claimant, User staff, User manager) {
        Instant now = Instant.now();
        Incident incident = baseIncident(
                "INC-DEMO-003", claimant.getId(), IncidentType.MOTOR_ACCIDENT,
                "Marina Bay, Singapore", "Synthetic assessed motor claim", now.minusSeconds(259_200));
        incidents.save(incident);

        Claim claim = baseClaim(
                "CLM-DEMO-003", incident.getIncidentId(), claimant.getId(),
                ProductType.MOTOR, ClaimStatus.APPROVED, staff.getId());
        claims.save(claim);
        claimHistory.save(history(
                claim.getClaimId(), ClaimStatus.READY_FOR_DECISION, ClaimStatus.APPROVED,
                "Demo manager approved the claim", manager.getId(), now.minusSeconds(3_600)));

        ClaimAssignment assignment = new ClaimAssignment();
        assignment.setClaimId(claim.getClaimId());
        assignment.setStaffId(staff.getId());
        assignment.setAssignedAt(now.minusSeconds(172_800));
        audit(assignment, staff.getId());
        assignments.save(assignment);

        Assessment assessment = new Assessment();
        assessment.setAssessmentId("ASM-DEMO-001");
        assessment.setClaimId(claim.getClaimId());
        assessment.setAssessorId(staff.getId());
        assessment.setMarket(Market.SG);
        assessment.setProductType(ProductType.MOTOR);
        assessment.setFindings("Synthetic demo assessment: supplied documents were verified.");
        assessment.setRecommendedDecision(RecommendedDecision.APPROVE);
        assessment.setRecommendationReason("Synthetic loss is within the configured demo limit.");
        assessment.setEstimatedLiability(new BigDecimal("12500.00"));
        assessment.setRiskFactors(List.of("Demo data only"));
        assessment.setStatus(AssessmentStatus.APPROVED_BY_MANAGER);
        assessment.setSubmittedAt(now.minusSeconds(7_200));
        audit(assessment, staff.getId());
        assessments.save(assessment);

        DecisionRecord decision = new DecisionRecord();
        decision.setDecisionId("DEC-DEMO-001");
        decision.setClaimId(claim.getClaimId());
        decision.setDecision(Decision.APPROVED);
        decision.setReason("Synthetic demo approval");
        decision.setDeciderId(manager.getId());
        decision.setDecisionDate(now.minusSeconds(3_600));
        audit(decision, manager.getId());
        decisions.save(decision);

        notifications.save(notification(
                "NTF-DEMO-002", NotificationEventType.DECISION_MADE,
                claim.getClaimId(), "Decision available for claim CLM-DEMO-003",
                "The decision for claim CLM-DEMO-003 is APPROVED.", now.minusSeconds(3_500)));
    }

    private Incident baseIncident(
            String incidentId, String claimantId, IncidentType type,
            String location, String description, Instant reportDate) {
        Incident incident = new Incident();
        incident.setIncidentId(incidentId);
        incident.setClaimantId(claimantId);
        incident.setReportDate(reportDate);
        incident.setIncidentType(type);
        incident.setLocation(location);
        incident.setDescription(description);
        incident.setMarket(Market.SG);
        incident.setStatus(IncidentStatus.CLAIM_CREATED);
        audit(incident, claimantId);

        ClaimItem item = new ClaimItem();
        item.setItemType(type == IncidentType.PROPERTY_DAMAGE
                ? ClaimItemType.PROPERTY : ClaimItemType.MOTOR_VEHICLE);
        item.setDescription("Synthetic demo insured item");
        item.setEstimatedValue(new BigDecimal("25000.00"));
        audit(item, claimantId);
        incident.addClaimItem(item);

        IncidentParty party = new IncidentParty();
        party.setPartyId("PTY-" + incidentId);
        party.setPartyType(IncidentPartyType.CLAIMANT);
        party.setFullName("Demo Claimant");
        party.setEmail("claimant.demo@example.com");
        party.setPhoneNumber("+65-0000-0000");
        party.setRelationshipType("POLICY_HOLDER");
        party.setStatement("Synthetic demonstration statement");
        audit(party, claimantId);
        incident.addParty(party);
        return incident;
    }

    private Claim baseClaim(
            String claimId, String incidentId, String claimantId,
            ProductType productType, ClaimStatus status, String assignedStaffId) {
        Claim claim = new Claim();
        claim.setClaimId(claimId);
        claim.setIncidentId(incidentId);
        claim.setClaimantId(claimantId);
        claim.setProductType(productType);
        claim.setMarket(Market.SG);
        claim.setStatus(status);
        claim.setAssignedStaffId(assignedStaffId);
        audit(claim, claimantId);
        return claim;
    }

    private ClaimStatusHistory history(
            String claimId, ClaimStatus oldStatus, ClaimStatus newStatus,
            String reason, String actor, Instant changedAt) {
        ClaimStatusHistory history = new ClaimStatusHistory();
        history.setClaimId(claimId);
        history.setOldStatus(oldStatus);
        history.setNewStatus(newStatus);
        history.setReason(reason);
        history.setChangedBy(actor);
        history.setChangedAt(changedAt);
        audit(history, actor);
        return history;
    }

    private NotificationLog notification(
            String notificationId, NotificationEventType eventType,
            String aggregateId, String subject, String message, Instant occurredAt) {
        NotificationLog logEntry = new NotificationLog();
        logEntry.setNotificationId(notificationId);
        logEntry.setEventType(eventType);
        logEntry.setAggregateId(aggregateId);
        logEntry.setChannel(NotificationChannel.IN_APP);
        logEntry.setStatus(NotificationStatus.CREATED);
        logEntry.setSubject(subject);
        logEntry.setMessage(message);
        logEntry.setCorrelationId("CORR-" + notificationId);
        logEntry.setOccurredAt(occurredAt);
        audit(logEntry, SYSTEM);
        return logEntry;
    }

    private void audit(com.chubb.apac.claims.modulith.common.model.BaseEntity entity, String actor) {
        entity.setCreatedBy(actor);
        entity.setUpdatedBy(actor);
    }
}
