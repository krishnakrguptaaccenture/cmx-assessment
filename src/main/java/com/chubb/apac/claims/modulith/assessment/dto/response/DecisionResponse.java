package com.chubb.apac.claims.modulith.assessment.dto.response;import com.chubb.apac.claims.modulith.common.enums.*;import java.time.Instant;
public record DecisionResponse(String decisionId,String claimId,Decision decision,String reason,String deciderId,String deciderName,Instant decisionDate,ClaimStatus claimStatus) {}
