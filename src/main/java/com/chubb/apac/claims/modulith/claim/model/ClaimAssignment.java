package com.chubb.apac.claims.modulith.claim.model;

import com.chubb.apac.claims.modulith.common.model.BaseEntity;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name="claim_assignments", indexes={@Index(name="idx_assignment_claim",columnList="claim_id"),@Index(name="idx_assignment_staff",columnList="staff_id")})
public class ClaimAssignment extends BaseEntity {
    @Column(name="claim_id",nullable=false,updatable=false,length=45) private String claimId;
    @Column(name="staff_id",nullable=false,updatable=false,length=36) private String staffId;
    @Column(name="assigned_at",nullable=false,updatable=false) private Instant assignedAt;
    @Column(name="unassigned_at") private Instant unassignedAt;
    public String getClaimId(){return claimId;} public void setClaimId(String v){claimId=v;}
    public String getStaffId(){return staffId;} public void setStaffId(String v){staffId=v;}
    public Instant getAssignedAt(){return assignedAt;} public void setAssignedAt(Instant v){assignedAt=v;}
    public Instant getUnassignedAt(){return unassignedAt;} public void setUnassignedAt(Instant v){unassignedAt=v;}
}
