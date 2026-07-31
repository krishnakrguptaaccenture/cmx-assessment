package com.chubb.apac.claims.modulith.inforequest.model;

import com.chubb.apac.claims.modulith.common.model.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "information_requests", indexes = {
        @Index(name = "idx_info_request_public_id", columnList = "request_id", unique = true),
        @Index(name = "idx_info_request_claim", columnList = "claim_id"),
        @Index(name = "idx_info_request_status_due", columnList = "status,due_date")
})
public class InformationRequest extends BaseEntity {
    @Column(name="request_id", nullable=false, unique=true, updatable=false, length=45)
    private String requestId;
    @Column(name="claim_id", nullable=false, updatable=false, length=45)
    private String claimId;
    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name="information_request_fields", joinColumns=@JoinColumn(name="information_request_fk"))
    @Column(name="requested_field", nullable=false, length=250)
    @OrderColumn(name="field_order")
    private List<String> requestedFields = new ArrayList<>();
    @Column(name="due_date", nullable=false)
    private LocalDate dueDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=30)
    private InformationRequestStatus status;
    @Column(length=2000)
    private String instructions;
    @Column(name="requested_by", nullable=false, updatable=false, length=36)
    private String requestedBy;

    public String getRequestId(){return requestId;} public void setRequestId(String v){requestId=v;}
    public String getClaimId(){return claimId;} public void setClaimId(String v){claimId=v;}
    public List<String> getRequestedFields(){return requestedFields;} public void setRequestedFields(List<String> v){requestedFields=new ArrayList<>(v);}
    public LocalDate getDueDate(){return dueDate;} public void setDueDate(LocalDate v){dueDate=v;}
    public InformationRequestStatus getStatus(){return status;} public void setStatus(InformationRequestStatus v){status=v;}
    public String getInstructions(){return instructions;} public void setInstructions(String v){instructions=v;}
    public String getRequestedBy(){return requestedBy;} public void setRequestedBy(String v){requestedBy=v;}
}
