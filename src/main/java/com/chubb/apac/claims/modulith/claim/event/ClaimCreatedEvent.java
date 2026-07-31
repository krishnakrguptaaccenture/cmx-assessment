package com.chubb.apac.claims.modulith.claim.event;
import com.chubb.apac.claims.modulith.common.enums.*;import java.time.Instant;
public record ClaimCreatedEvent(String eventId,String claimId,String incidentId,String claimantId,ProductType productType,Market market,Instant occurredAt,String correlationId) {}
