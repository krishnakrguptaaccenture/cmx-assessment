package com.chubb.apac.claims.modulith.incident.model;
import com.chubb.apac.claims.modulith.common.model.BaseEntity;
import jakarta.persistence.*;
@Entity @Table(name="incident_parties", indexes=@Index(name="idx_incident_party_incident", columnList="incident_fk"))
public class IncidentParty extends BaseEntity {
 @Column(name="party_id",nullable=false,unique=true,updatable=false,length=40) private String partyId;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="incident_fk",nullable=false) private Incident incident;
 @Enumerated(EnumType.STRING) @Column(name="party_type",nullable=false,length=30) private IncidentPartyType partyType;
 @Column(name="full_name",nullable=false,length=150) private String fullName;
 @Column(nullable=false,length=254) private String email;
 @Column(name="phone_number",length=30) private String phoneNumber;
 @Column(name="relationship_type",length=100) private String relationshipType;
 @Column(length=4000) private String statement;
 public String getPartyId(){return partyId;} public void setPartyId(String v){partyId=v;}
 public Incident getIncident(){return incident;} public void setIncident(Incident v){incident=v;}
 public IncidentPartyType getPartyType(){return partyType;} public void setPartyType(IncidentPartyType v){partyType=v;}
 public String getFullName(){return fullName;} public void setFullName(String v){fullName=v;}
 public String getEmail(){return email;} public void setEmail(String v){email=v;}
 public String getPhoneNumber(){return phoneNumber;} public void setPhoneNumber(String v){phoneNumber=v;}
 public String getRelationshipType(){return relationshipType;} public void setRelationshipType(String v){relationshipType=v;}
 public String getStatement(){return statement;} public void setStatement(String v){statement=v;}
}
