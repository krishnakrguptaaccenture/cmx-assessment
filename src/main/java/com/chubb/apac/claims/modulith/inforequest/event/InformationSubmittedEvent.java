package com.chubb.apac.claims.modulith.inforequest.event;
import java.time.Instant;
public record InformationSubmittedEvent(String eventId,String claimId,String requestId,String responseId,String submittedBy,
 Instant occurredAt,String correlationId) {}
