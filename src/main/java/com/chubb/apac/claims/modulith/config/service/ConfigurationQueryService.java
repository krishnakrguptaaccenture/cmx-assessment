package com.chubb.apac.claims.modulith.config.service;
import com.chubb.apac.claims.modulith.common.enums.*;import com.chubb.apac.claims.modulith.config.dto.*;import java.util.List;
public interface ConfigurationQueryService {List<MarketConfigurationResponse> markets();List<ProductConfigurationResponse> products(Market market);List<BusinessRuleResponse> rules(Market market,ProductType productType);SlaConfigurationResponse sla(Market market,ProductType productType);ProductConfigurationResponse requireProduct(Market market,ProductType productType);}
