package com.chubb.apac.claims.modulith.notification.dto;

import com.chubb.apac.claims.modulith.notification.model.NotificationEventType;
import java.time.Instant;
import java.util.Map;

public record NotificationCommand(
        NotificationEventType eventType,
        String aggregateId,
        Instant occurredAt,
        String correlationId,
        Map<String,String> variables) {
    public NotificationCommand {
        variables = variables == null ? Map.of() : Map.copyOf(variables);
    }
}
