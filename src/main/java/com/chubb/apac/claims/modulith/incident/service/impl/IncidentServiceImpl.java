package com.chubb.apac.claims.modulith.incident.service.impl;
import com.chubb.apac.claims.modulith.common.exception.*;
import com.chubb.apac.claims.modulith.incident.dto.request.*;
import com.chubb.apac.claims.modulith.incident.dto.response.*;
import com.chubb.apac.claims.modulith.incident.event.*;
import com.chubb.apac.claims.modulith.incident.mapper.IncidentMapper;
import com.chubb.apac.claims.modulith.incident.model.*;
import com.chubb.apac.claims.modulith.incident.repository.*;
import com.chubb.apac.claims.modulith.incident.service.*;
import java.time.Instant;
import java.util.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service @Transactional
public class IncidentServiceImpl implements IncidentService {
 private final IncidentRepository incidents; private final IncidentPartyRepository parties; private final IncidentMapper mapper;
 private final IncidentIdGenerator ids; private final ApplicationEventPublisher events;
 public IncidentServiceImpl(IncidentRepository incidents,IncidentPartyRepository parties,IncidentMapper mapper,IncidentIdGenerator ids,ApplicationEventPublisher events){this.incidents=incidents;this.parties=parties;this.mapper=mapper;this.ids=ids;this.events=events;}
 public IncidentResponse report(String claimantId,String correlationId,CreateIncidentRequest r){
  Incident i=new Incident();i.setIncidentId(ids.nextIncidentId());i.setClaimantId(claimantId);i.setReportDate(r.reportDate());i.setIncidentType(r.incidentType());i.setLocation(r.location().trim());i.setDescription(r.description().trim());i.setMarket(r.market());i.setStatus(IncidentStatus.REPORTED);i.setCreatedBy(claimantId);i.setUpdatedBy(claimantId);
  for(ClaimItemRequest x:r.claimItems()){ClaimItem item=new ClaimItem();item.setItemType(x.itemType());item.setDescription(x.description().trim());item.setEstimatedValue(x.estimatedValue());item.setCreatedBy(claimantId);item.setUpdatedBy(claimantId);i.addClaimItem(item);}
  incidents.saveAndFlush(i);events.publishEvent(new IncidentReportedEvent(UUID.randomUUID().toString(),i.getIncidentId(),claimantId,i.getIncidentType(),i.getMarket(),i.getReportDate(),Instant.now(),normaliseCorrelation(correlationId)));return mapper.toResponse(i);
 }
 @Transactional(readOnly=true) public IncidentResponse getOwned(String claimantId,String incidentId){return mapper.toResponse(findOwned(claimantId,incidentId));}
 public IncidentResponse updateOwned(String claimantId,String incidentId,String correlationId,UpdateIncidentRequest r){Incident i=findOwned(claimantId,incidentId);if(i.getStatus()!=IncidentStatus.REPORTED)throw new ConflictException("Only a reported incident can be updated");if(r.description()==null&&r.location()==null)throw new BusinessValidationException("At least one field must be supplied");if(r.description()!=null)i.setDescription(r.description().trim());if(r.location()!=null)i.setLocation(r.location().trim());i.setUpdatedBy(claimantId);events.publishEvent(new IncidentUpdatedEvent(UUID.randomUUID().toString(),incidentId,claimantId,Instant.now(),normaliseCorrelation(correlationId)));return mapper.toResponse(i);}
 public IncidentPartyResponse addParty(String claimantId,String incidentId,CreateIncidentPartyRequest r){Incident i=findOwned(claimantId,incidentId);if(i.getStatus()!=IncidentStatus.REPORTED)throw new ConflictException("Parties cannot be added after incident review starts");IncidentParty p=new IncidentParty();p.setPartyId(ids.nextPartyId());p.setPartyType(r.partyType());p.setFullName(r.fullName().trim());p.setEmail(r.email().trim().toLowerCase(Locale.ROOT));p.setPhoneNumber(r.phoneNumber());p.setRelationshipType(r.relationshipType());p.setStatement(r.statement());p.setCreatedBy(claimantId);p.setUpdatedBy(claimantId);i.addParty(p);parties.saveAndFlush(p);return mapper.toResponse(p);}
 @Transactional(readOnly=true) public List<IncidentPartyResponse> listParties(String claimantId,String incidentId){findOwned(claimantId,incidentId);return mapper.toPartyResponses(parties.findByIncidentIncidentIdOrderByCreatedAtAsc(incidentId));}
 private Incident findOwned(String claimantId,String incidentId){return incidents.findByIncidentIdAndClaimantId(incidentId,claimantId).orElseThrow(()->new ResourceNotFoundException("Incident not found"));}
 private String normaliseCorrelation(String value){return value==null||value.isBlank()?UUID.randomUUID().toString():value;}
}
