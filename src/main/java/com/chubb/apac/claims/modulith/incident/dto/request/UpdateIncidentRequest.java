package com.chubb.apac.claims.modulith.incident.dto.request;
import jakarta.validation.constraints.Size;
public record UpdateIncidentRequest(@Size(min=1,max=4000) String description,@Size(min=1,max=250) String location) {}
