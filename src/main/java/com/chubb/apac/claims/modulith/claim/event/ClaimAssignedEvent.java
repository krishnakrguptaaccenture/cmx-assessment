package com.chubb.apac.claims.modulith.claim.event;
import com.chubb.apac.claims.modulith.common.enums.Market;import java.time.Instant;
public record ClaimAssignedEvent(String eventId,String claimId,String staffId,Market market,Instant occurredAt,String correlationId) {}
