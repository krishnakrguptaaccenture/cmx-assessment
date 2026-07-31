package com.chubb.apac.claims.modulith.claim.mapper;
import com.chubb.apac.claims.modulith.claim.dto.response.*;import com.chubb.apac.claims.modulith.claim.model.*;import java.util.List;import org.springframework.stereotype.Component;
@Component public class ClaimMapper {
 public ClaimResponse toResponse(Claim c){return new ClaimResponse(c.getClaimId(),c.getIncidentId(),c.getStatus(),c.getProductType(),c.getMarket(),c.getAssignedStaffId(),c.getCreatedAt(),c.getUpdatedAt());}
 public ClaimStatusHistoryResponse toResponse(ClaimStatusHistory h){return new ClaimStatusHistoryResponse(h.getOldStatus(),h.getNewStatus(),h.getReason(),h.getChangedBy(),h.getChangedAt());}
 public ClaimDetailResponse toDetail(Claim c,List<ClaimStatusHistory> history){return new ClaimDetailResponse(c.getClaimId(),c.getIncidentId(),c.getClaimantId(),c.getStatus(),c.getProductType(),c.getMarket(),c.getAssignedStaffId(),null,history.stream().map(this::toResponse).toList(),c.getCreatedAt(),c.getUpdatedAt());}
}
