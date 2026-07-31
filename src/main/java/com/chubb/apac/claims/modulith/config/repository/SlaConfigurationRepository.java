package com.chubb.apac.claims.modulith.config.repository;
import com.chubb.apac.claims.modulith.common.enums.*;import com.chubb.apac.claims.modulith.config.model.SlaConfiguration;
import java.util.Optional;import org.springframework.data.jpa.repository.JpaRepository;
public interface SlaConfigurationRepository extends JpaRepository<SlaConfiguration,String>{Optional<SlaConfiguration> findByMarketAndProductTypeAndActiveTrue(Market market,ProductType productType);boolean existsByMarketAndProductType(Market market,ProductType productType);}
