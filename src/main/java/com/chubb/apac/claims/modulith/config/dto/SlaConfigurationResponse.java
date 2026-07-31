package com.chubb.apac.claims.modulith.config.dto;
import com.chubb.apac.claims.modulith.common.enums.*;
public record SlaConfigurationResponse(Market market,ProductType productType,int assessmentSlaDays,int decisionSlaDays) {}
