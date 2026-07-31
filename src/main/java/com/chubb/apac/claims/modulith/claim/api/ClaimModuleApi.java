package com.chubb.apac.claims.modulith.claim.api;
import com.chubb.apac.claims.modulith.common.enums.Market;
import java.util.Set;
public interface ClaimModuleApi {
    ClaimAccessView requireClaimantAccess(String claimId,String claimantId);
    ClaimAccessView requireStaffAccess(String claimId,String staffId,Set<Market> markets);
}
