package com.chubb.apac.claims.modulith.config.model;

import com.chubb.apac.claims.modulith.common.enums.*;
import com.chubb.apac.claims.modulith.common.model.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name="business_rules", uniqueConstraints=@UniqueConstraint(name="uk_rule_market_product_key",columnNames={"market","product_type","rule_key"}),
       indexes=@Index(name="idx_rule_market_product_active",columnList="market,product_type,active"))
public class BusinessRule extends BaseEntity {
    @Enumerated(EnumType.STRING) @Column(nullable=false,updatable=false,length=2) private Market market;
    @Enumerated(EnumType.STRING) @Column(name="product_type",nullable=false,updatable=false,length=20) private ProductType productType;
    @Column(name="rule_key",nullable=false,updatable=false,length=100) private String ruleKey;
    @Column(name="rule_value",nullable=false,length=1000) private String ruleValue;
    @Column(nullable=false,length=1000) private String description;
    @Column(nullable=false) private boolean active=true;
    public Market getMarket(){return market;} public void setMarket(Market v){market=v;}
    public ProductType getProductType(){return productType;} public void setProductType(ProductType v){productType=v;}
    public String getRuleKey(){return ruleKey;} public void setRuleKey(String v){ruleKey=v;}
    public String getRuleValue(){return ruleValue;} public void setRuleValue(String v){ruleValue=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
}
