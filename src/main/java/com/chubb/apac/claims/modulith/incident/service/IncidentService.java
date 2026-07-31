package com.chubb.apac.claims.modulith.incident.service;
import com.chubb.apac.claims.modulith.incident.dto.request.*;
import com.chubb.apac.claims.modulith.incident.dto.response.*;
import java.util.List;
public interface IncidentService {
 IncidentResponse report(String claimantId,String correlationId,CreateIncidentRequest request);
 IncidentResponse getOwned(String claimantId,String incidentId);
 IncidentResponse updateOwned(String claimantId,String incidentId,String correlationId,UpdateIncidentRequest request);
 IncidentPartyResponse addParty(String claimantId,String incidentId,CreateIncidentPartyRequest request);
 List<IncidentPartyResponse> listParties(String claimantId,String incidentId);
}
