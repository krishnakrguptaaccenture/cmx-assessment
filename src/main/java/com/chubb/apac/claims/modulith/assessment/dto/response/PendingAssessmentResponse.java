package com.chubb.apac.claims.modulith.assessment.dto.response;import com.chubb.apac.claims.modulith.assessment.model.RecommendedDecision;import java.time.Instant;
public record PendingAssessmentResponse(String assessmentId,String claimId,String claimantName,String assessorName,RecommendedDecision recommendedDecision,String findings,Instant submittedAt,long daysInReview) {}
