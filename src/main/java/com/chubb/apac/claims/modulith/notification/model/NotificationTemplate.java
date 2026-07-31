package com.chubb.apac.claims.modulith.notification.model;

import com.chubb.apac.claims.modulith.common.model.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name="notification_templates", uniqueConstraints=@UniqueConstraint(
        name="uk_notification_template_event_channel",
        columnNames={"event_type","channel"}))
public class NotificationTemplate extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name="event_type",nullable=false,updatable=false,length=40)
    private NotificationEventType eventType;
    @Enumerated(EnumType.STRING)
    @Column(nullable=false,updatable=false,length=20)
    private NotificationChannel channel;
    @Column(nullable=false,length=250)
    private String subjectTemplate;
    @Column(nullable=false,length=4000)
    private String bodyTemplate;
    @Column(nullable=false)
    private boolean active=true;

    public NotificationEventType getEventType(){return eventType;}
    public void setEventType(NotificationEventType value){eventType=value;}
    public NotificationChannel getChannel(){return channel;}
    public void setChannel(NotificationChannel value){channel=value;}
    public String getSubjectTemplate(){return subjectTemplate;}
    public void setSubjectTemplate(String value){subjectTemplate=value;}
    public String getBodyTemplate(){return bodyTemplate;}
    public void setBodyTemplate(String value){bodyTemplate=value;}
    public boolean isActive(){return active;}
    public void setActive(boolean value){active=value;}
}
