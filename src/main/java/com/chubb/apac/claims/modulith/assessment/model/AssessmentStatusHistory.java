package com.chubb.apac.claims.modulith.assessment.model;
import com.chubb.apac.claims.modulith.common.model.BaseEntity;import jakarta.persistence.*;import java.time.Instant;
@Entity @Table(name="assessment_status_history",indexes=@Index(name="idx_assessment_history",columnList="assessment_id,changed_at"))
public class AssessmentStatusHistory extends BaseEntity {
 @Column(name="assessment_id",nullable=false,updatable=false,length=45) private String assessmentId;
 @Enumerated(EnumType.STRING) @Column(name="old_status",length=30) private AssessmentStatus oldStatus;
 @Enumerated(EnumType.STRING) @Column(name="new_status",nullable=false,length=30) private AssessmentStatus newStatus;
 @Column(nullable=false,length=500) private String reason;@Column(name="changed_by",nullable=false,length=36) private String changedBy;@Column(name="changed_at",nullable=false) private Instant changedAt;
 public String getAssessmentId(){return assessmentId;}public void setAssessmentId(String v){assessmentId=v;}public AssessmentStatus getOldStatus(){return oldStatus;}public void setOldStatus(AssessmentStatus v){oldStatus=v;}public AssessmentStatus getNewStatus(){return newStatus;}public void setNewStatus(AssessmentStatus v){newStatus=v;}public String getReason(){return reason;}public void setReason(String v){reason=v;}public String getChangedBy(){return changedBy;}public void setChangedBy(String v){changedBy=v;}public Instant getChangedAt(){return changedAt;}public void setChangedAt(Instant v){changedAt=v;}
}
