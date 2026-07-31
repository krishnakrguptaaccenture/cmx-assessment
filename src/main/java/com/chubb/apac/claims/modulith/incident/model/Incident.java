package com.chubb.apac.claims.modulith.incident.model;

import com.chubb.apac.claims.modulith.common.enums.Market;
import com.chubb.apac.claims.modulith.common.model.BaseEntity;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "incidents", indexes = {
    @Index(name = "idx_incident_public_id", columnList = "incident_id", unique = true),
    @Index(name = "idx_incident_claimant", columnList = "claimant_id"),
    @Index(name = "idx_incident_market", columnList = "market")
})
public class Incident extends BaseEntity {
    @Column(name = "incident_id", nullable = false, unique = true, updatable = false, length = 40)
    private String incidentId;
    @Column(name = "claimant_id", nullable = false, updatable = false, length = 36)
    private String claimantId;
    @Column(name = "report_date", nullable = false)
    private Instant reportDate;
    @Enumerated(EnumType.STRING) @Column(name = "incident_type", nullable = false, length = 40)
    private IncidentType incidentType;
    @Column(nullable = false, length = 250)
    private String location;
    @Column(nullable = false, length = 4000)
    private String description;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 2)
    private Market market;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30)
    private IncidentStatus status;
    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClaimItem> claimItems = new ArrayList<>();
    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IncidentParty> parties = new ArrayList<>();

    public void addClaimItem(ClaimItem item){ claimItems.add(item); item.setIncident(this); }
    public void addParty(IncidentParty party){ parties.add(party); party.setIncident(this); }
    public String getIncidentId(){return incidentId;} public void setIncidentId(String v){incidentId=v;}
    public String getClaimantId(){return claimantId;} public void setClaimantId(String v){claimantId=v;}
    public Instant getReportDate(){return reportDate;} public void setReportDate(Instant v){reportDate=v;}
    public IncidentType getIncidentType(){return incidentType;} public void setIncidentType(IncidentType v){incidentType=v;}
    public String getLocation(){return location;} public void setLocation(String v){location=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public Market getMarket(){return market;} public void setMarket(Market v){market=v;}
    public IncidentStatus getStatus(){return status;} public void setStatus(IncidentStatus v){status=v;}
    public List<ClaimItem> getClaimItems(){return claimItems;} public List<IncidentParty> getParties(){return parties;}
}
