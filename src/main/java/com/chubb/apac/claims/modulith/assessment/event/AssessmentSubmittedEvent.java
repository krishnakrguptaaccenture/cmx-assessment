package com.chubb.apac.claims.modulith.assessment.event;import java.time.Instant;
public record AssessmentSubmittedEvent(String eventId,String assessmentId,String claimId,String assessorId,Instant occurredAt,String correlationId){}
