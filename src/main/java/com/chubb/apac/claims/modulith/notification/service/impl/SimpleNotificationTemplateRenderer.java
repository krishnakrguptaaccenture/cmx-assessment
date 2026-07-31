package com.chubb.apac.claims.modulith.notification.service.impl;

import com.chubb.apac.claims.modulith.notification.service.NotificationTemplateRenderer;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SimpleNotificationTemplateRenderer implements NotificationTemplateRenderer {
    @Override
    public String render(String template,Map<String,String> variables){
        String rendered=template;
        for(Map.Entry<String,String> entry:variables.entrySet()){
            rendered=rendered.replace("{{"+entry.getKey()+"}}",safe(entry.getValue()));
        }
        return rendered;
    }
    private String safe(String value){return value==null?"":value;}
}
