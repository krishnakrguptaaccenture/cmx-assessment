package com.chubb.apac.claims.modulith.config.repository;
import com.chubb.apac.claims.modulith.common.enums.Market;import com.chubb.apac.claims.modulith.config.model.MarketConfiguration;
import java.util.*;import org.springframework.data.jpa.repository.JpaRepository;
public interface MarketConfigurationRepository extends JpaRepository<MarketConfiguration,String>{List<MarketConfiguration> findByActiveTrueOrderByMarketAsc();Optional<MarketConfiguration> findByMarketAndActiveTrue(Market market);boolean existsByMarket(Market market);}
