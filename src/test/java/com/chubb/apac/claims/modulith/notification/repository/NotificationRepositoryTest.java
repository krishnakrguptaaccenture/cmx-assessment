package com.chubb.apac.claims.modulith.notification.repository;

import com.chubb.apac.claims.modulith.notification.model.*;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class NotificationRepositoryTest {
    @Autowired NotificationTemplateRepository templates;
    @Autowired NotificationLogRepository logs;
    @Test void storesTemplateAndLog(){
        NotificationTemplate t=new NotificationTemplate();
        t.setEventType(NotificationEventType.DECISION_MADE);
        t.setChannel(NotificationChannel.IN_APP);
        t.setSubjectTemplate("Subject");t.setBodyTemplate("Body");
        t.setCreatedBy("test");t.setUpdatedBy("test");
        templates.saveAndFlush(t);
        NotificationLog l=new NotificationLog();
        l.setNotificationId("NTF-1");l.setEventType(NotificationEventType.DECISION_MADE);
        l.setAggregateId("CLM-1");l.setChannel(NotificationChannel.IN_APP);
        l.setStatus(NotificationStatus.CREATED);l.setSubject("Subject");l.setMessage("Body");
        l.setCorrelationId("C1");l.setOccurredAt(Instant.now());
        l.setCreatedBy("test");l.setUpdatedBy("test");
        logs.saveAndFlush(l);
        assertThat(logs.findByAggregateIdOrderByOccurredAtDesc("CLM-1")).hasSize(1);
    }
}
