package com.chubb.apac.claims.modulith.incident.mapper;
import com.chubb.apac.claims.modulith.incident.dto.response.*;
import com.chubb.apac.claims.modulith.incident.model.*;
import java.util.List;
import org.springframework.stereotype.Component;
@Component
public class IncidentMapper {
 public IncidentResponse toResponse(Incident i){return new IncidentResponse(i.getIncidentId(),i.getClaimantId(),i.getReportDate(),i.getIncidentType(),i.getLocation(),i.getDescription(),i.getMarket(),i.getStatus(),i.getClaimItems().stream().map(this::toResponse).toList(),i.getCreatedAt(),i.getUpdatedAt());}
 public ClaimItemResponse toResponse(ClaimItem i){return new ClaimItemResponse(i.getItemType(),i.getDescription(),i.getEstimatedValue());}
 public IncidentPartyResponse toResponse(IncidentParty p){return new IncidentPartyResponse(p.getPartyId(),p.getIncident().getIncidentId(),p.getPartyType(),p.getFullName(),p.getEmail(),p.getPhoneNumber(),p.getRelationshipType(),p.getStatement(),p.getCreatedAt());}
 public List<IncidentPartyResponse> toPartyResponses(List<IncidentParty> parties){return parties.stream().map(this::toResponse).toList();}
}
