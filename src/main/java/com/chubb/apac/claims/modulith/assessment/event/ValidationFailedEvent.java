package com.chubb.apac.claims.modulith.assessment.event;import java.time.Instant;import java.util.List;
public record ValidationFailedEvent(String eventId,String assessmentId,String claimId,List<String> errorCodes,Instant occurredAt,String correlationId){}
