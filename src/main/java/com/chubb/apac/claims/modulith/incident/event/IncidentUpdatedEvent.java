package com.chubb.apac.claims.modulith.incident.event;
import java.time.Instant;
public record IncidentUpdatedEvent(String eventId,String incidentId,String claimantId,Instant occurredAt,String correlationId) {}
