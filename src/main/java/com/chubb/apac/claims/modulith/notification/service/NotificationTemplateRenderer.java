package com.chubb.apac.claims.modulith.notification.service;

import java.util.Map;

public interface NotificationTemplateRenderer {
    String render(String template,Map<String,String> variables);
}
