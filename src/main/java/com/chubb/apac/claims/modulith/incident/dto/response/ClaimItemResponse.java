package com.chubb.apac.claims.modulith.incident.dto.response;
import com.chubb.apac.claims.modulith.incident.model.ClaimItemType;
import java.math.BigDecimal;
public record ClaimItemResponse(ClaimItemType itemType,String description,BigDecimal estimatedValue) {}
