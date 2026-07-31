package com.chubb.apac.claims.modulith.notification.model;

import com.chubb.apac.claims.modulith.common.model.BaseEntity;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name="notification_log", indexes={
        @Index(name="idx_notification_aggregate",columnList="aggregate_id"),
        @Index(name="idx_notification_event_status",columnList="event_type,status"),
        @Index(name="idx_notification_correlation",columnList="correlation_id")
})
public class NotificationLog extends BaseEntity {
    @Column(name="notification_id",nullable=false,unique=true,updatable=false,length=45)
    private String notificationId;
    @Enumerated(EnumType.STRING)
    @Column(name="event_type",nullable=false,updatable=false,length=40)
    private NotificationEventType eventType;
    @Column(name="aggregate_id",nullable=false,updatable=false,length=45)
    private String aggregateId;
    @Enumerated(EnumType.STRING)
    @Column(nullable=false,updatable=false,length=20)
    private NotificationChannel channel;
    @Enumerated(EnumType.STRING)
    @Column(nullable=false,length=20)
    private NotificationStatus status;
    @Column(nullable=false,length=250)
    private String subject;
    @Column(nullable=false,length=4000)
    private String message;
    @Column(name="correlation_id",nullable=false,updatable=false,length=100)
    private String correlationId;
    @Column(name="occurred_at",nullable=false,updatable=false)
    private Instant occurredAt;
    @Column(name="failure_reason",length=1000)
    private String failureReason;

    public String getNotificationId(){return notificationId;}
    public void setNotificationId(String value){notificationId=value;}
    public NotificationEventType getEventType(){return eventType;}
    public void setEventType(NotificationEventType value){eventType=value;}
    public String getAggregateId(){return aggregateId;}
    public void setAggregateId(String value){aggregateId=value;}
    public NotificationChannel getChannel(){return channel;}
    public void setChannel(NotificationChannel value){channel=value;}
    public NotificationStatus getStatus(){return status;}
    public void setStatus(NotificationStatus value){status=value;}
    public String getSubject(){return subject;}
    public void setSubject(String value){subject=value;}
    public String getMessage(){return message;}
    public void setMessage(String value){message=value;}
    public String getCorrelationId(){return correlationId;}
    public void setCorrelationId(String value){correlationId=value;}
    public Instant getOccurredAt(){return occurredAt;}
    public void setOccurredAt(Instant value){occurredAt=value;}
    public String getFailureReason(){return failureReason;}
    public void setFailureReason(String value){failureReason=value;}
}
