package com.chubb.apac.claims.modulith.config.repository;
import com.chubb.apac.claims.modulith.common.enums.*;import com.chubb.apac.claims.modulith.config.model.BusinessRule;
import java.util.List;import org.springframework.data.jpa.repository.JpaRepository;
public interface BusinessRuleRepository extends JpaRepository<BusinessRule,String>{List<BusinessRule> findByMarketAndProductTypeAndActiveTrueOrderByRuleKeyAsc(Market market,ProductType productType);boolean existsByMarketAndProductTypeAndRuleKey(Market market,ProductType productType,String ruleKey);}
