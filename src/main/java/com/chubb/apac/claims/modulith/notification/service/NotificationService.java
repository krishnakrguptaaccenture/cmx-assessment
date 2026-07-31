package com.chubb.apac.claims.modulith.notification.service;
import com.chubb.apac.claims.modulith.notification.dto.*;
import com.chubb.apac.claims.modulith.notification.model.NotificationChannel;
public interface NotificationService {
    NotificationResult create(NotificationCommand command,NotificationChannel channel);
}
