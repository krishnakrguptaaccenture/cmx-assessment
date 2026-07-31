package com.chubb.apac.claims.modulith.incident.event;
import com.chubb.apac.claims.modulith.common.enums.Market;
import com.chubb.apac.claims.modulith.incident.model.IncidentType;
import java.time.Instant;
public record IncidentReportedEvent(String eventId,String incidentId,String claimantId,IncidentType incidentType,Market market,
 Instant reportDate,Instant occurredAt,String correlationId) {}
