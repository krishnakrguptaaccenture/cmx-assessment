package com.chubb.apac.claims.modulith.claim.api;
import com.chubb.apac.claims.modulith.common.enums.*;
public record ClaimAccessView(String claimId,String claimantId,ClaimStatus status,Market market,ProductType productType,String assignedStaffId) {}
