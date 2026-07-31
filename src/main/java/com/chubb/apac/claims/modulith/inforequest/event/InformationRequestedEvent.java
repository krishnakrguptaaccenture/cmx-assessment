package com.chubb.apac.claims.modulith.inforequest.event;
import java.time.*;import java.util.List;
public record InformationRequestedEvent(String eventId,String claimId,String requestId,List<String> requestedFields,
 LocalDate dueDate,String requestedBy,Instant occurredAt,String correlationId) {}
