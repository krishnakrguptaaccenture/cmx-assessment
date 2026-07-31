package com.chubb.apac.claims.modulith.claim.event;
import com.chubb.apac.claims.modulith.common.enums.ClaimStatus;import java.time.Instant;
public record ClaimStatusChangedEvent(String eventId,String claimId,ClaimStatus oldStatus,ClaimStatus newStatus,String reason,String changedBy,Instant occurredAt,String correlationId) {}
