package com.chubb.apac.claims.modulith.notification.repository;

import com.chubb.apac.claims.modulith.notification.model.*;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate,String> {
    Optional<NotificationTemplate> findByEventTypeAndChannelAndActiveTrue(
            NotificationEventType eventType, NotificationChannel channel);
    boolean existsByEventTypeAndChannel(NotificationEventType eventType,NotificationChannel channel);
}
