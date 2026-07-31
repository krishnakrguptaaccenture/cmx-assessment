package com.chubb.apac.claims.modulith.assessment.event;import com.chubb.apac.claims.modulith.common.enums.*;import java.math.BigDecimal;import java.time.Instant;
public record DecisionMadeEvent(String eventId,String claimId,Decision decision,String reason,String deciderId,Instant decisionDate,BigDecimal approvedAmount,Market market,ProductType productType,String correlationId){}
