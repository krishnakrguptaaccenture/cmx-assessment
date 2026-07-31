package com.chubb.apac.claims.modulith.config.model;

import com.chubb.apac.claims.modulith.common.enums.Market;
import com.chubb.apac.claims.modulith.common.model.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "markets", uniqueConstraints = @UniqueConstraint(name = "uk_market_code", columnNames = "market_code"))
public class MarketConfiguration extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "market_code", nullable = false, unique = true, updatable = false, length = 2)
    private Market market;
    @Column(nullable = false, length = 100) private String name;
    @Column(nullable = false, length = 3) private String currency;
    @Column(nullable = false, length = 10) private String language;
    @Column(nullable = false, length = 60) private String timezone;
    @Column(nullable = false) private boolean active = true;
    public Market getMarket(){return market;} public void setMarket(Market v){market=v;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getCurrency(){return currency;} public void setCurrency(String v){currency=v;}
    public String getLanguage(){return language;} public void setLanguage(String v){language=v;}
    public String getTimezone(){return timezone;} public void setTimezone(String v){timezone=v;}
    public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
}
