package com.chubb.apac.claims.modulith.claim.model;

import com.chubb.apac.claims.modulith.common.enums.ClaimStatus;
import com.chubb.apac.claims.modulith.common.enums.Market;
import com.chubb.apac.claims.modulith.common.enums.ProductType;
import com.chubb.apac.claims.modulith.common.model.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "claims", indexes = {
        @Index(name = "idx_claim_public_id", columnList = "claim_id", unique = true),
        @Index(name = "idx_claim_incident", columnList = "incident_id", unique = true),
        @Index(name = "idx_claim_claimant", columnList = "claimant_id"),
        @Index(name = "idx_claim_market_status", columnList = "market,status"),
        @Index(name = "idx_claim_assignee", columnList = "assigned_staff_id")
})
public class Claim extends BaseEntity {
    @Column(name = "claim_id", nullable = false, unique = true, updatable = false, length = 45)
    private String claimId;
    @Column(name = "incident_id", nullable = false, unique = true, updatable = false, length = 45)
    private String incidentId;
    @Column(name = "claimant_id", nullable = false, updatable = false, length = 36)
    private String claimantId;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30)
    private ClaimStatus status;
    @Enumerated(EnumType.STRING) @Column(name = "product_type", nullable = false, length = 20)
    private ProductType productType;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 2)
    private Market market;
    @Column(name = "assigned_staff_id", length = 36)
    private String assignedStaffId;

    public String getClaimId(){return claimId;} public void setClaimId(String v){claimId=v;}
    public String getIncidentId(){return incidentId;} public void setIncidentId(String v){incidentId=v;}
    public String getClaimantId(){return claimantId;} public void setClaimantId(String v){claimantId=v;}
    public ClaimStatus getStatus(){return status;} public void setStatus(ClaimStatus v){status=v;}
    public ProductType getProductType(){return productType;} public void setProductType(ProductType v){productType=v;}
    public Market getMarket(){return market;} public void setMarket(Market v){market=v;}
    public String getAssignedStaffId(){return assignedStaffId;} public void setAssignedStaffId(String v){assignedStaffId=v;}
}
