package com.chubb.apac.claims.modulith.config.dto;
import com.chubb.apac.claims.modulith.common.enums.Market;
public record MarketConfigurationResponse(Market marketId,String name,String currency,String language,String timezone) {}
