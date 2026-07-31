package com.chubb.apac.claims.modulith.inforequest.model;

import com.chubb.apac.claims.modulith.common.model.BaseEntity;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name="information_responses", indexes={
        @Index(name="idx_info_response_public_id", columnList="response_id", unique=true),
        @Index(name="idx_info_response_request", columnList="request_id")
})
public class InformationResponse extends BaseEntity {
    @Column(name="response_id", nullable=false, unique=true, updatable=false, length=45)
    private String responseId;
    @Column(name="request_id", nullable=false, updatable=false, length=45)
    private String requestId;
    @Column(name="claim_id", nullable=false, updatable=false, length=45)
    private String claimId;
    @Column(nullable=false, length=8000)
    private String response;
    @Column(name="submitted_at", nullable=false, updatable=false)
    private Instant submittedAt;
    @Column(name="submitted_by", nullable=false, updatable=false, length=36)
    private String submittedBy;
    public String getResponseId(){return responseId;} public void setResponseId(String v){responseId=v;}
    public String getRequestId(){return requestId;} public void setRequestId(String v){requestId=v;}
    public String getClaimId(){return claimId;} public void setClaimId(String v){claimId=v;}
    public String getResponse(){return response;} public void setResponse(String v){response=v;}
    public Instant getSubmittedAt(){return submittedAt;} public void setSubmittedAt(Instant v){submittedAt=v;}
    public String getSubmittedBy(){return submittedBy;} public void setSubmittedBy(String v){submittedBy=v;}
}
