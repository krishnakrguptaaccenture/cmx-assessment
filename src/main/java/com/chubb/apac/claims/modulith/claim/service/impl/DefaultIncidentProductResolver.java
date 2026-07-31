package com.chubb.apac.claims.modulith.claim.service.impl;
import com.chubb.apac.claims.modulith.claim.service.IncidentProductResolver;import com.chubb.apac.claims.modulith.common.enums.ProductType;import com.chubb.apac.claims.modulith.incident.model.IncidentType;import org.springframework.stereotype.Component;
@Component public class DefaultIncidentProductResolver implements IncidentProductResolver {
 public ProductType resolve(IncidentType type){return switch(type){case MOTOR_ACCIDENT,THIRD_PARTY_CLAIM->ProductType.MOTOR;case PROPERTY_DAMAGE->ProductType.PROPERTY;};}
}
