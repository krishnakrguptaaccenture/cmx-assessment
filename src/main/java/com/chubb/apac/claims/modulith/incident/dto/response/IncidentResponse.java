package com.chubb.apac.claims.modulith.incident.dto.response;
import com.chubb.apac.claims.modulith.common.enums.Market;
import com.chubb.apac.claims.modulith.incident.model.*;
import java.time.*;
import java.util.List;
public record IncidentResponse(String incidentId,String claimantId,Instant reportDate,IncidentType incidentType,String location,
 String description,Market market,IncidentStatus status,List<ClaimItemResponse> claimItems,LocalDateTime createdAt,LocalDateTime updatedAt) {}
