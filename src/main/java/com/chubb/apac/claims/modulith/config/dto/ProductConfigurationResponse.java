package com.chubb.apac.claims.modulith.config.dto;
import com.chubb.apac.claims.modulith.common.enums.*;import java.math.BigDecimal;
public record ProductConfigurationResponse(Market market,ProductType type,BigDecimal claimLimitMin,BigDecimal claimLimitMax) {}
