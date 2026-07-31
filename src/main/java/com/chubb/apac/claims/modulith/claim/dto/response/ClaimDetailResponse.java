package com.chubb.apac.claims.modulith.claim.dto.response;
import com.chubb.apac.claims.modulith.common.enums.*;
import java.time.LocalDateTime;import java.util.List;
public record ClaimDetailResponse(String claimId,String incidentId,String claimantId,ClaimStatus status,ProductType productType,
 Market market,String assignedStaffId,String assignedStaffName,List<ClaimStatusHistoryResponse> statusHistory,
 LocalDateTime createdAt,LocalDateTime updatedAt) {}
