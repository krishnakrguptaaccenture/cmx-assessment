package com.chubb.apac.claims.modulith.notification.service.impl;

import com.chubb.apac.claims.modulith.notification.dto.*;
import com.chubb.apac.claims.modulith.notification.model.*;
import com.chubb.apac.claims.modulith.notification.repository.*;
import com.chubb.apac.claims.modulith.notification.service.*;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationTemplateRepository templates;
    private final NotificationLogRepository logs;
    private final NotificationTemplateRenderer renderer;
    private final NotificationIdGenerator ids;

    public NotificationServiceImpl(NotificationTemplateRepository templates,
            NotificationLogRepository logs,NotificationTemplateRenderer renderer,
            NotificationIdGenerator ids){
        this.templates=templates;this.logs=logs;this.renderer=renderer;this.ids=ids;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public NotificationResult create(NotificationCommand command,NotificationChannel channel){
        NotificationTemplate template=templates
                .findByEventTypeAndChannelAndActiveTrue(command.eventType(),channel)
                .orElse(null);
        NotificationLog log=new NotificationLog();
        log.setNotificationId(ids.nextNotificationId());
        log.setEventType(command.eventType());
        log.setAggregateId(command.aggregateId());
        log.setChannel(channel);
        log.setCorrelationId(correlation(command.correlationId()));
        log.setOccurredAt(command.occurredAt()==null?Instant.now():command.occurredAt());
        log.setCreatedBy("notification-module");
        log.setUpdatedBy("notification-module");
        if(template==null){
            log.setStatus(NotificationStatus.SKIPPED);
            log.setSubject("Notification skipped");
            log.setMessage("No active template is configured for this event and channel");
        }else{
            log.setStatus(NotificationStatus.CREATED);
            log.setSubject(renderer.render(template.getSubjectTemplate(),command.variables()));
            log.setMessage(renderer.render(template.getBodyTemplate(),command.variables()));
        }
        logs.saveAndFlush(log);
        return new NotificationResult(log.getNotificationId(),log.getEventType(),
                log.getAggregateId(),log.getChannel(),log.getStatus(),log.getOccurredAt());
    }
    private String correlation(String value){return value==null||value.isBlank()?UUID.randomUUID().toString():value;}
}
