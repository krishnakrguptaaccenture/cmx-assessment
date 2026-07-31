package com.chubb.apac.claims.modulith.claim.dto.response;
import com.chubb.apac.claims.modulith.common.enums.ClaimStatus;
import java.time.Instant;
public record ClaimStatusHistoryResponse(ClaimStatus oldStatus,ClaimStatus newStatus,String reason,String changedBy,Instant changedAt) {}
