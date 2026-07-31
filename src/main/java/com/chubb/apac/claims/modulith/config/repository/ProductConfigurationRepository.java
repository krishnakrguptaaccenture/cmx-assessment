package com.chubb.apac.claims.modulith.config.repository;
import com.chubb.apac.claims.modulith.common.enums.*;import com.chubb.apac.claims.modulith.config.model.ProductConfiguration;
import java.util.*;import org.springframework.data.jpa.repository.JpaRepository;
public interface ProductConfigurationRepository extends JpaRepository<ProductConfiguration,String>{List<ProductConfiguration> findByMarketAndActiveTrueOrderByProductTypeAsc(Market market);Optional<ProductConfiguration> findByMarketAndProductTypeAndActiveTrue(Market market,ProductType productType);boolean existsByMarketAndProductType(Market market,ProductType productType);}
