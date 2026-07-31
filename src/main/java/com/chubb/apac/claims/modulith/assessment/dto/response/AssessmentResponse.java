package com.chubb.apac.claims.modulith.assessment.dto.response;
import com.chubb.apac.claims.modulith.assessment.model.*;import java.math.BigDecimal;import java.time.*;import java.util.List;
public record AssessmentResponse(String assessmentId,String claimId,String assessorId,String assessorName,String findings,RecommendedDecision recommendedDecision,String recommendationReason,BigDecimal estimatedLiability,List<String> riskFactors,AssessmentStatus status,Instant submittedAt,LocalDateTime createdAt) {}
