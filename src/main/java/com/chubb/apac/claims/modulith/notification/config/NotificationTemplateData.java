package com.chubb.apac.claims.modulith.notification.config;

import com.chubb.apac.claims.modulith.notification.model.*;
import com.chubb.apac.claims.modulith.notification.repository.NotificationTemplateRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;

@Configuration
public class NotificationTemplateData {
    @Bean
    @ConditionalOnProperty(name="cmx.notification.seed-enabled",havingValue="true",matchIfMissing=true)
    CommandLineRunner seedNotificationTemplates(NotificationTemplateRepository templates){
        return args->{
            seed(templates,NotificationEventType.INFORMATION_REQUESTED,
                    "Additional information requested for claim {{claimId}}",
                    "Information request {{requestId}} is due on {{dueDate}}.");
            seed(templates,NotificationEventType.INFORMATION_SUBMITTED,
                    "Information received for claim {{claimId}}",
                    "A response was submitted for information request {{requestId}}.");
            seed(templates,NotificationEventType.DECISION_MADE,
                    "Decision available for claim {{claimId}}",
                    "The decision for claim {{claimId}} is {{decision}}.");
        };
    }
    private static void seed(NotificationTemplateRepository templates,
            NotificationEventType eventType,String subject,String body){
        if(!templates.existsByEventTypeAndChannel(eventType,NotificationChannel.IN_APP)){
            NotificationTemplate template=new NotificationTemplate();
            template.setEventType(eventType);template.setChannel(NotificationChannel.IN_APP);
            template.setSubjectTemplate(subject);template.setBodyTemplate(body);
            template.setCreatedBy("seed");template.setUpdatedBy("seed");
            templates.save(template);
        }
    }
}
