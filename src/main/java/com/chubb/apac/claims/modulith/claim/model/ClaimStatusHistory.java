package com.chubb.apac.claims.modulith.claim.model;

import com.chubb.apac.claims.modulith.common.enums.ClaimStatus;
import com.chubb.apac.claims.modulith.common.model.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name="claim_status_history", indexes=@Index(name="idx_claim_history_claim", columnList="claim_id,changed_at"))
public class ClaimStatusHistory extends BaseEntity {
    @Column(name="claim_id", nullable=false, updatable=false, length=45) private String claimId;
    @Enumerated(EnumType.STRING) @Column(name="old_status", length=30) private ClaimStatus oldStatus;
    @Enumerated(EnumType.STRING) @Column(name="new_status", nullable=false, length=30) private ClaimStatus newStatus;
    @Column(nullable=false, length=500) private String reason;
    @Column(name="changed_by", nullable=false, updatable=false, length=100) private String changedBy;
    @Column(name="changed_at", nullable=false, updatable=false) private java.time.Instant changedAt;
    public String getClaimId(){return claimId;} public void setClaimId(String v){claimId=v;}
    public ClaimStatus getOldStatus(){return oldStatus;} public void setOldStatus(ClaimStatus v){oldStatus=v;}
    public ClaimStatus getNewStatus(){return newStatus;} public void setNewStatus(ClaimStatus v){newStatus=v;}
    public String getReason(){return reason;} public void setReason(String v){reason=v;}
    public String getChangedBy(){return changedBy;} public void setChangedBy(String v){changedBy=v;}
    public java.time.Instant getChangedAt(){return changedAt;} public void setChangedAt(java.time.Instant v){changedAt=v;}
}
