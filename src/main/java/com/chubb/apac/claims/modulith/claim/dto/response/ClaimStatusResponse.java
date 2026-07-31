package com.chubb.apac.claims.modulith.claim.dto.response;
import com.chubb.apac.claims.modulith.common.enums.ClaimStatus;
import java.time.LocalDateTime;
public record ClaimStatusResponse(String claimId,ClaimStatus status,LocalDateTime lastUpdated) {}
