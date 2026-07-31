package com.chubb.apac.claims.modulith.claim.service;
import com.chubb.apac.claims.modulith.claim.dto.response.*;import com.chubb.apac.claims.modulith.common.dto.PageResponse;import com.chubb.apac.claims.modulith.common.enums.*;import java.util.Set;import org.springframework.data.domain.Pageable;
public interface ClaimService {
 PageResponse<ClaimResponse> listForClaimant(String claimantId,ClaimStatus status,Pageable pageable);
 ClaimDetailResponse getForClaimant(String claimantId,String claimId);
 ClaimStatusResponse getStatusForClaimant(String claimantId,String claimId);
 PageResponse<ClaimResponse> listForStaff(Set<Market> permittedMarkets,ClaimStatus status,Market market,ProductType productType,String assignedTo,Pageable pageable);
 ClaimDetailResponse getForStaff(Set<Market> permittedMarkets,String claimId);
 ClaimAssignmentResponse assignToSelf(Set<Market> permittedMarkets,String staffId,String claimId,String correlationId);
 ClaimUnassignmentResponse unassignFromSelf(Set<Market> permittedMarkets,String staffId,String claimId,String correlationId);
}
