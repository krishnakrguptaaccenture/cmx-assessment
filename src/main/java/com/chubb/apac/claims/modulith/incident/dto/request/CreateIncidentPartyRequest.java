package com.chubb.apac.claims.modulith.incident.dto.request;
import com.chubb.apac.claims.modulith.incident.model.IncidentPartyType;
import jakarta.validation.constraints.*;
public record CreateIncidentPartyRequest(@NotNull IncidentPartyType partyType,@NotBlank @Size(max=150) String fullName,
 @NotBlank @Email @Size(max=254) String email,@Size(max=30) String phoneNumber,@Size(max=100) String relationshipType,
 @Size(max=4000) String statement) {}
