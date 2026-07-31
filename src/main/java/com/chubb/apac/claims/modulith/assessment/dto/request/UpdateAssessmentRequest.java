package com.chubb.apac.claims.modulith.assessment.dto.request;
import com.chubb.apac.claims.modulith.assessment.model.RecommendedDecision;import jakarta.validation.constraints.*;import java.math.BigDecimal;import java.util.List;
public record UpdateAssessmentRequest(@Size(min=1,max=8000) String findings,RecommendedDecision recommendedDecision,@Size(min=1,max=4000) String recommendationReason,@DecimalMin("0.00") BigDecimal estimatedLiability,List<@NotBlank @Size(max=500) String> riskFactors){}
