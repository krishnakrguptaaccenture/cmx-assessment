package com.chubb.apac.claims.modulith.inforequest.service.impl;
import com.chubb.apac.claims.modulith.claim.api.ClaimModuleApi;
import com.chubb.apac.claims.modulith.common.enums.Market;
import com.chubb.apac.claims.modulith.common.exception.*;
import com.chubb.apac.claims.modulith.inforequest.dto.request.*;
import com.chubb.apac.claims.modulith.inforequest.dto.response.*;
import com.chubb.apac.claims.modulith.inforequest.event.*;
import com.chubb.apac.claims.modulith.inforequest.mapper.InformationRequestMapper;
import com.chubb.apac.claims.modulith.inforequest.model.*;
import com.chubb.apac.claims.modulith.inforequest.repository.*;
import com.chubb.apac.claims.modulith.inforequest.service.*;
import java.time.Instant;import java.util.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service @Transactional
public class InformationRequestServiceImpl implements InformationRequestService {
 private final InformationRequestRepository requests;private final InformationResponseRepository responses;
 private final ClaimModuleApi claims;private final InformationRequestMapper mapper;private final InformationRequestIdGenerator ids;
 private final ApplicationEventPublisher events;
 public InformationRequestServiceImpl(InformationRequestRepository requests,InformationResponseRepository responses,
 ClaimModuleApi claims,InformationRequestMapper mapper,InformationRequestIdGenerator ids,ApplicationEventPublisher events){
 this.requests=requests;this.responses=responses;this.claims=claims;this.mapper=mapper;this.ids=ids;this.events=events;}
 public InformationRequestResponse create(String claimId,String staffId,Set<Market> markets,String correlationId,CreateInformationRequest r){
  claims.requireStaffAccess(claimId,staffId,markets);
  if(requests.existsByClaimIdAndStatusIn(claimId,Set.of(InformationRequestStatus.PENDING,InformationRequestStatus.CLARIFICATION_NEEDED)))
   throw new ConflictException("An active information request already exists for this claim");
  List<String> fields=normaliseFields(r.requestedFields());InformationRequest q=new InformationRequest();q.setRequestId(ids.nextRequestId());q.setClaimId(claimId);q.setRequestedFields(fields);q.setDueDate(r.dueDate());q.setStatus(InformationRequestStatus.PENDING);q.setInstructions(trimToNull(r.instructions()));q.setRequestedBy(staffId);q.setCreatedBy(staffId);q.setUpdatedBy(staffId);requests.saveAndFlush(q);
  events.publishEvent(new InformationRequestedEvent(UUID.randomUUID().toString(),claimId,q.getRequestId(),fields,q.getDueDate(),staffId,Instant.now(),correlation(correlationId)));
  return mapper.toResponse(q,List.of());
 }
 @Transactional(readOnly=true) public List<InformationRequestResponse> listForStaff(String claimId,String staffId,Set<Market> markets){claims.requireStaffAccess(claimId,staffId,markets);return list(claimId);}
 @Transactional(readOnly=true) public List<InformationRequestResponse> listForClaimant(String claimId,String claimantId){claims.requireClaimantAccess(claimId,claimantId);return list(claimId);}
 public InformationResponseResponse submit(String claimId,String requestId,String claimantId,String correlationId,SubmitInformationResponse r){
  claims.requireClaimantAccess(claimId,claimantId);InformationRequest q=requests.findForUpdate(requestId,claimId).orElseThrow(()->new ResourceNotFoundException("Information request not found"));
  if(q.getStatus()!=InformationRequestStatus.PENDING&&q.getStatus()!=InformationRequestStatus.CLARIFICATION_NEEDED)
   throw new ConflictException("Information request does not accept a response in its current state");
  InformationResponse x=new InformationResponse();x.setResponseId(ids.nextResponseId());x.setRequestId(requestId);x.setClaimId(claimId);x.setResponse(r.response().trim());x.setSubmittedAt(Instant.now());x.setSubmittedBy(claimantId);x.setCreatedBy(claimantId);x.setUpdatedBy(claimantId);responses.saveAndFlush(x);q.setStatus(InformationRequestStatus.SUBMITTED);q.setUpdatedBy(claimantId);
  events.publishEvent(new InformationSubmittedEvent(UUID.randomUUID().toString(),claimId,requestId,x.getResponseId(),claimantId,x.getSubmittedAt(),correlation(correlationId)));
  return mapper.toResponse(x);
 }
 private List<InformationRequestResponse> list(String claimId){return requests.findByClaimIdOrderByCreatedAtDesc(claimId).stream().map(q->mapper.toResponse(q,responses.findByRequestIdOrderBySubmittedAtAsc(q.getRequestId()))).toList();}
 private List<String> normaliseFields(List<String> values){LinkedHashSet<String> out=new LinkedHashSet<>();for(String value:values){String v=value.trim();if(!v.isEmpty())out.add(v);}if(out.isEmpty())throw new BusinessValidationException("At least one requested field is required");return List.copyOf(out);}
 private String trimToNull(String v){return v==null||v.isBlank()?null:v.trim();}
 private String correlation(String v){return v==null||v.isBlank()?UUID.randomUUID().toString():v;}
}
