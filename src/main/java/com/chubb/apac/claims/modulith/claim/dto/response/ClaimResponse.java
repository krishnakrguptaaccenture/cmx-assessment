package com.chubb.apac.claims.modulith.claim.dto.response;
import com.chubb.apac.claims.modulith.common.enums.*;
import java.time.LocalDateTime;
public record ClaimResponse(String claimId,String incidentId,ClaimStatus status,ProductType productType,Market market,
 String assignedStaffId,LocalDateTime createdAt,LocalDateTime updatedAt) {}
