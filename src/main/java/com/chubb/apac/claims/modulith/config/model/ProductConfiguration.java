package com.chubb.apac.claims.modulith.config.model;

import com.chubb.apac.claims.modulith.common.enums.*;
import com.chubb.apac.claims.modulith.common.model.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="product_types", uniqueConstraints=@UniqueConstraint(name="uk_product_market_type",columnNames={"market","product_type"}),
       indexes=@Index(name="idx_product_market_active",columnList="market,active"))
public class ProductConfiguration extends BaseEntity {
    @Enumerated(EnumType.STRING) @Column(nullable=false,updatable=false,length=2) private Market market;
    @Enumerated(EnumType.STRING) @Column(name="product_type",nullable=false,updatable=false,length=20) private ProductType productType;
    @Column(name="claim_limit_min",nullable=false,precision=19,scale=2) private BigDecimal claimLimitMin;
    @Column(name="claim_limit_max",nullable=false,precision=19,scale=2) private BigDecimal claimLimitMax;
    @Column(nullable=false) private boolean active=true;
    public Market getMarket(){return market;} public void setMarket(Market v){market=v;}
    public ProductType getProductType(){return productType;} public void setProductType(ProductType v){productType=v;}
    public BigDecimal getClaimLimitMin(){return claimLimitMin;} public void setClaimLimitMin(BigDecimal v){claimLimitMin=v;}
    public BigDecimal getClaimLimitMax(){return claimLimitMax;} public void setClaimLimitMax(BigDecimal v){claimLimitMax=v;}
    public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
}
