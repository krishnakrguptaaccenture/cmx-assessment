package com.chubb.apac.claims.modulith.claim.service;
import com.chubb.apac.claims.modulith.common.enums.ProductType;import com.chubb.apac.claims.modulith.incident.model.IncidentType;
public interface IncidentProductResolver {ProductType resolve(IncidentType incidentType);}
