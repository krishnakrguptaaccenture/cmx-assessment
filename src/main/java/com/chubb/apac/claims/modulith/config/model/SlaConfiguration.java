package com.chubb.apac.claims.modulith.config.model;

import com.chubb.apac.claims.modulith.common.enums.*;
import com.chubb.apac.claims.modulith.common.model.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name="sla_configurations", uniqueConstraints=@UniqueConstraint(name="uk_sla_market_product",columnNames={"market","product_type"}))
public class SlaConfiguration extends BaseEntity {
    @Enumerated(EnumType.STRING) @Column(nullable=false,updatable=false,length=2) private Market market;
    @Enumerated(EnumType.STRING) @Column(name="product_type",nullable=false,updatable=false,length=20) private ProductType productType;
    @Column(name="assessment_sla_days",nullable=false) private int assessmentSlaDays;
    @Column(name="decision_sla_days",nullable=false) private int decisionSlaDays;
    @Column(nullable=false) private boolean active=true;
    public Market getMarket(){return market;} public void setMarket(Market v){market=v;}
    public ProductType getProductType(){return productType;} public void setProductType(ProductType v){productType=v;}
    public int getAssessmentSlaDays(){return assessmentSlaDays;} public void setAssessmentSlaDays(int v){assessmentSlaDays=v;}
    public int getDecisionSlaDays(){return decisionSlaDays;} public void setDecisionSlaDays(int v){decisionSlaDays=v;}
    public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
}
