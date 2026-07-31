package com.chubb.apac.claims.modulith.incident.service.impl;
import com.chubb.apac.claims.modulith.incident.service.IncidentIdGenerator;
import java.util.UUID;
import org.springframework.stereotype.Component;
@Component
public class UuidIncidentIdGenerator implements IncidentIdGenerator {
 public String nextIncidentId(){return "INC-"+UUID.randomUUID().toString().toUpperCase();}
 public String nextPartyId(){return "PTY-"+UUID.randomUUID().toString().toUpperCase();}
}
