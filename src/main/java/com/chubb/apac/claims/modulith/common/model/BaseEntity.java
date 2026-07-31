package com.chubb.apac.claims.modulith.common.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
@MappedSuperclass
public abstract class BaseEntity {
 @Id @Column(nullable=false,updatable=false,length=36) private String id;
 @Column(nullable=false,updatable=false) private LocalDateTime createdAt;
 @Column(nullable=false) private LocalDateTime updatedAt;
 @Column(nullable=false,updatable=false,length=100) private String createdBy;
 @Column(nullable=false,length=100) private String updatedBy;
 @Version private long version;
 @PrePersist protected void prePersist(){LocalDateTime now=LocalDateTime.now();if(id==null)id=UUID.randomUUID().toString();createdAt=now;updatedAt=now;if(createdBy==null)createdBy="system";if(updatedBy==null)updatedBy=createdBy;}
 @PreUpdate protected void preUpdate(){updatedAt=LocalDateTime.now();}
 public String getId(){return id;} public void setId(String id){this.id=id;} public LocalDateTime getCreatedAt(){return createdAt;} public LocalDateTime getUpdatedAt(){return updatedAt;} public String getCreatedBy(){return createdBy;} public void setCreatedBy(String v){createdBy=v;} public String getUpdatedBy(){return updatedBy;} public void setUpdatedBy(String v){updatedBy=v;} public long getVersion(){return version;}
}
