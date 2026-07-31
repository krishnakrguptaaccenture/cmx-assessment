package com.chubb.apac.claims.modulith.incident.dto.request;
import com.chubb.apac.claims.modulith.common.enums.Market;
import com.chubb.apac.claims.modulith.incident.model.IncidentType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.Instant;
import java.util.List;
public record CreateIncidentRequest(@NotNull @PastOrPresent Instant reportDate,@NotNull IncidentType incidentType,
 @NotBlank @Size(max=250) String location,@NotBlank @Size(max=4000) String description,@NotNull Market market,
 @Valid List<ClaimItemRequest> claimItems) { public CreateIncidentRequest { claimItems=claimItems==null?List.of():List.copyOf(claimItems); } }
