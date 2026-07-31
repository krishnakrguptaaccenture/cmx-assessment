package com.chubb.apac.claims.modulith.incident.model;
import com.chubb.apac.claims.modulith.common.model.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity @Table(name="claim_items", indexes=@Index(name="idx_claim_item_incident", columnList="incident_fk"))
public class ClaimItem extends BaseEntity {
 @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="incident_fk", nullable=false) private Incident incident;
 @Enumerated(EnumType.STRING) @Column(name="item_type",nullable=false,length=30) private ClaimItemType itemType;
 @Column(nullable=false,length=1000) private String description;
 @Column(name="estimated_value",precision=19,scale=2) private BigDecimal estimatedValue;
 public Incident getIncident(){return incident;} public void setIncident(Incident v){incident=v;}
 public ClaimItemType getItemType(){return itemType;} public void setItemType(ClaimItemType v){itemType=v;}
 public String getDescription(){return description;} public void setDescription(String v){description=v;}
 public BigDecimal getEstimatedValue(){return estimatedValue;} public void setEstimatedValue(BigDecimal v){estimatedValue=v;}
}
