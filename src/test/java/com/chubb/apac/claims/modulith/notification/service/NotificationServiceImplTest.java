package com.chubb.apac.claims.modulith.notification.service;

import com.chubb.apac.claims.modulith.notification.dto.*;
import com.chubb.apac.claims.modulith.notification.model.*;
import com.chubb.apac.claims.modulith.notification.repository.*;
import com.chubb.apac.claims.modulith.notification.service.impl.*;
import java.time.Instant;import java.util.*;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificationServiceImplTest {
    @Test void createsNotificationFromTemplate(){
        NotificationTemplateRepository templates=mock(NotificationTemplateRepository.class);
        NotificationLogRepository logs=mock(NotificationLogRepository.class);
        NotificationIdGenerator ids=mock(NotificationIdGenerator.class);
        NotificationTemplate template=new NotificationTemplate();
        template.setEventType(NotificationEventType.DECISION_MADE);
        template.setChannel(NotificationChannel.IN_APP);
        template.setSubjectTemplate("Decision for {{claimId}}");
        template.setBodyTemplate("Decision: {{decision}}");
        when(templates.findByEventTypeAndChannelAndActiveTrue(
                NotificationEventType.DECISION_MADE,NotificationChannel.IN_APP))
                .thenReturn(Optional.of(template));
        when(ids.nextNotificationId()).thenReturn("NTF-1");
        when(logs.saveAndFlush(any())).thenAnswer(x->x.getArgument(0));
        NotificationService service=new NotificationServiceImpl(templates,logs,
                new SimpleNotificationTemplateRenderer(),ids);
        NotificationResult out=service.create(new NotificationCommand(
                NotificationEventType.DECISION_MADE,"CLM-1",Instant.now(),"C1",
                Map.of("claimId","CLM-1","decision","APPROVED")),
                NotificationChannel.IN_APP);
        assertThat(out.status()).isEqualTo(NotificationStatus.CREATED);
        verify(logs).saveAndFlush(argThat(log->log.getSubject().equals("Decision for CLM-1")));
    }

    @Test void logsSkippedWhenTemplateMissing(){
        NotificationTemplateRepository templates=mock(NotificationTemplateRepository.class);
        NotificationLogRepository logs=mock(NotificationLogRepository.class);
        NotificationIdGenerator ids=mock(NotificationIdGenerator.class);
        when(ids.nextNotificationId()).thenReturn("NTF-1");
        when(logs.saveAndFlush(any())).thenAnswer(x->x.getArgument(0));
        NotificationService service=new NotificationServiceImpl(templates,logs,
                new SimpleNotificationTemplateRenderer(),ids);
        NotificationResult out=service.create(new NotificationCommand(
                NotificationEventType.INFORMATION_SUBMITTED,"CLM-1",Instant.now(),"C1",Map.of()),
                NotificationChannel.IN_APP);
        assertThat(out.status()).isEqualTo(NotificationStatus.SKIPPED);
    }
}
