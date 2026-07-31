package com.chubb.apac.claims.modulith.notification.service.impl;
import com.chubb.apac.claims.modulith.notification.service.NotificationIdGenerator;
import java.util.UUID;
import org.springframework.stereotype.Component;
@Component public class UuidNotificationIdGenerator implements NotificationIdGenerator {
    public String nextNotificationId(){return "NTF-"+UUID.randomUUID().toString().toUpperCase();}
}
