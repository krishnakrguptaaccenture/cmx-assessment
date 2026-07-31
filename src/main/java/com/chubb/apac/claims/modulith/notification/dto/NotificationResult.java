package com.chubb.apac.claims.modulith.notification.dto;

import com.chubb.apac.claims.modulith.notification.model.*;
import java.time.Instant;

public record NotificationResult(
        String notificationId,
        NotificationEventType eventType,
        String aggregateId,
        NotificationChannel channel,
        NotificationStatus status,
        Instant occurredAt) {}
