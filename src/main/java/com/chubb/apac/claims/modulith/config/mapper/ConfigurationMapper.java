package com.chubb.apac.claims.modulith.config.mapper;
import com.chubb.apac.claims.modulith.config.dto.*;import com.chubb.apac.claims.modulith.config.model.*;import org.springframework.stereotype.Component;
@Component public class ConfigurationMapper {
 public MarketConfigurationResponse toResponse(MarketConfiguration x){return new MarketConfigurationResponse(x.getMarket(),x.getName(),x.getCurrency(),x.getLanguage(),x.getTimezone());}
 public ProductConfigurationResponse toResponse(ProductConfiguration x){return new ProductConfigurationResponse(x.getMarket(),x.getProductType(),x.getClaimLimitMin(),x.getClaimLimitMax());}
 public BusinessRuleResponse toResponse(BusinessRule x){return new BusinessRuleResponse(x.getMarket(),x.getProductType(),x.getRuleKey(),x.getRuleValue(),x.getDescription());}
 public SlaConfigurationResponse toResponse(SlaConfiguration x){return new SlaConfigurationResponse(x.getMarket(),x.getProductType(),x.getAssessmentSlaDays(),x.getDecisionSlaDays());}
}
