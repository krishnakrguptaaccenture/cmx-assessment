package com.chubb.apac.claims.modulith.claim.service;
import com.chubb.apac.claims.modulith.incident.event.IncidentReportedEvent;
public interface ClaimCreationService {void createFromIncident(IncidentReportedEvent event);}
