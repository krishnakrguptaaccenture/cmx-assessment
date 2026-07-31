package com.chubb.apac.claims.modulith.config.service.impl;
import com.chubb.apac.claims.modulith.common.enums.*;import com.chubb.apac.claims.modulith.common.exception.ResourceNotFoundException;import com.chubb.apac.claims.modulith.config.dto.*;import com.chubb.apac.claims.modulith.config.mapper.ConfigurationMapper;import com.chubb.apac.claims.modulith.config.repository.*;import com.chubb.apac.claims.modulith.config.service.ConfigurationQueryService;import java.util.List;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;
@Service @Transactional(readOnly=true)
public class ConfigurationQueryServiceImpl implements ConfigurationQueryService {
 private final MarketConfigurationRepository markets;private final ProductConfigurationRepository products;private final BusinessRuleRepository rules;private final SlaConfigurationRepository slas;private final ConfigurationMapper mapper;
 public ConfigurationQueryServiceImpl(MarketConfigurationRepository markets,ProductConfigurationRepository products,BusinessRuleRepository rules,SlaConfigurationRepository slas,ConfigurationMapper mapper){this.markets=markets;this.products=products;this.rules=rules;this.slas=slas;this.mapper=mapper;}
 public List<MarketConfigurationResponse> markets(){return markets.findByActiveTrueOrderByMarketAsc().stream().map(mapper::toResponse).toList();}
 public List<ProductConfigurationResponse> products(Market market){requireMarket(market);return products.findByMarketAndActiveTrueOrderByProductTypeAsc(market).stream().map(mapper::toResponse).toList();}
 public List<BusinessRuleResponse> rules(Market market,ProductType product){requireProduct(market,product);return rules.findByMarketAndProductTypeAndActiveTrueOrderByRuleKeyAsc(market,product).stream().map(mapper::toResponse).toList();}
 public SlaConfigurationResponse sla(Market market,ProductType product){requireProduct(market,product);return mapper.toResponse(slas.findByMarketAndProductTypeAndActiveTrue(market,product).orElseThrow(()->new ResourceNotFoundException("SLA configuration not found")));}
 public ProductConfigurationResponse requireProduct(Market market,ProductType product){requireMarket(market);return mapper.toResponse(products.findByMarketAndProductTypeAndActiveTrue(market,product).orElseThrow(()->new ResourceNotFoundException("Product configuration not found")));}
 private void requireMarket(Market market){markets.findByMarketAndActiveTrue(market).orElseThrow(()->new ResourceNotFoundException("Market configuration not found"));}
}
