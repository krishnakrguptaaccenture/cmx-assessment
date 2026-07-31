package com.chubb.apac.claims.modulith.assessment.model;
import com.chubb.apac.claims.modulith.common.model.BaseEntity;
import com.chubb.apac.claims.modulith.common.enums.*;
import jakarta.persistence.*;import java.math.BigDecimal;import java.time.Instant;import java.util.*;
@Entity @Table(name="assessments",indexes={@Index(name="idx_assessment_claim",columnList="claim_id",unique=true),@Index(name="idx_assessment_status_submitted",columnList="status,submitted_at")})
public class Assessment extends BaseEntity {
 @Column(name="assessment_id",nullable=false,unique=true,updatable=false,length=45) private String assessmentId;
 @Column(name="claim_id",nullable=false,unique=true,updatable=false,length=45) private String claimId;
 @Column(name="assessor_id",nullable=false,updatable=false,length=36) private String assessorId;
 @Enumerated(EnumType.STRING) @Column(nullable=false,updatable=false,length=2) private Market market;
 @Enumerated(EnumType.STRING) @Column(name="product_type",nullable=false,updatable=false,length=20) private ProductType productType;
 @Column(nullable=false,length=8000) private String findings;
 @Enumerated(EnumType.STRING) @Column(name="recommended_decision",nullable=false,length=30) private RecommendedDecision recommendedDecision;
 @Column(name="recommendation_reason",nullable=false,length=4000) private String recommendationReason;
 @Column(name="estimated_liability",precision=19,scale=2) private BigDecimal estimatedLiability;
 @ElementCollection(fetch=FetchType.EAGER) @CollectionTable(name="assessment_risk_factors",joinColumns=@JoinColumn(name="assessment_fk")) @Column(name="risk_factor",nullable=false,length=500) @OrderColumn(name="factor_order") private List<String> riskFactors=new ArrayList<>();
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=30) private AssessmentStatus status;
 @Column(name="submitted_at") private Instant submittedAt;
 public Market getMarket(){return market;}public void setMarket(Market v){market=v;}public ProductType getProductType(){return productType;}public void setProductType(ProductType v){productType=v;}
 public String getAssessmentId(){return assessmentId;}public void setAssessmentId(String v){assessmentId=v;}public String getClaimId(){return claimId;}public void setClaimId(String v){claimId=v;}public String getAssessorId(){return assessorId;}public void setAssessorId(String v){assessorId=v;}public String getFindings(){return findings;}public void setFindings(String v){findings=v;}public RecommendedDecision getRecommendedDecision(){return recommendedDecision;}public void setRecommendedDecision(RecommendedDecision v){recommendedDecision=v;}public String getRecommendationReason(){return recommendationReason;}public void setRecommendationReason(String v){recommendationReason=v;}public BigDecimal getEstimatedLiability(){return estimatedLiability;}public void setEstimatedLiability(BigDecimal v){estimatedLiability=v;}public List<String> getRiskFactors(){return riskFactors;}public void setRiskFactors(List<String> v){riskFactors=new ArrayList<>(v);}public AssessmentStatus getStatus(){return status;}public void setStatus(AssessmentStatus v){status=v;}public Instant getSubmittedAt(){return submittedAt;}public void setSubmittedAt(Instant v){submittedAt=v;}
}
