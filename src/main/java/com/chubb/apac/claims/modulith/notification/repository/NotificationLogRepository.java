package com.chubb.apac.claims.modulith.notification.repository;

import com.chubb.apac.claims.modulith.notification.model.NotificationLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationLogRepository extends JpaRepository<NotificationLog,String> {
    List<NotificationLog> findByAggregateIdOrderByOccurredAtDesc(String aggregateId);
}
