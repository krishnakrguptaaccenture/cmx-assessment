package com.chubb.apac.claims.modulith.incident.dto.response;
import com.chubb.apac.claims.modulith.incident.model.IncidentPartyType;
import java.time.LocalDateTime;
public record IncidentPartyResponse(String partyId,String incidentId,IncidentPartyType partyType,String fullName,String email,
 String phoneNumber,String relationshipType,String statement,LocalDateTime createdAt) {}
