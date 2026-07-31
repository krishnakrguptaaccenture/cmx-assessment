package com.chubb.apac.claims.modulith.claim.api;
import com.chubb.apac.claims.modulith.common.enums.*;import java.util.Set;
public interface ClaimWorkflowApi {
 ClaimAccessView requireAssignedStaffClaim(String claimId,String staffId,Set<Market> markets);
 ClaimAccessView requireManagerClaim(String claimId,Set<Market> markets);
 ClaimAccessView transition(String claimId,Set<Market> markets,ClaimStatus expected,ClaimStatus next,String reason,String actor,String correlationId);
}
