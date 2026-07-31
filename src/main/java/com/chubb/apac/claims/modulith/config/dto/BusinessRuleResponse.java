package com.chubb.apac.claims.modulith.config.dto;
import com.chubb.apac.claims.modulith.common.enums.*;
public record BusinessRuleResponse(Market market,ProductType productType,String ruleKey,String ruleValue,String description) {}
