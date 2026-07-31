package com.chubb.apac.claims.modulith.claim.api;
import com.chubb.apac.claims.modulith.claim.model.Claim;
import com.chubb.apac.claims.modulith.claim.repository.ClaimRepository;
import com.chubb.apac.claims.modulith.common.enums.Market;
import com.chubb.apac.claims.modulith.common.exception.*;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service @Transactional(readOnly=true)
public class ClaimModuleApiImpl implements ClaimModuleApi {
    private final ClaimRepository claims;
    public ClaimModuleApiImpl(ClaimRepository claims){this.claims=claims;}
    public ClaimAccessView requireClaimantAccess(String claimId,String claimantId){
        return view(claims.findByClaimIdAndClaimantId(claimId,claimantId)
                .orElseThrow(()->new ResourceNotFoundException("Claim not found")));
    }
    public ClaimAccessView requireStaffAccess(String claimId,String staffId,Set<Market> markets){
        if(markets==null||markets.isEmpty())throw new ForbiddenException("No market access is assigned");
        Claim c=claims.findByClaimIdAndMarketIn(claimId,markets)
                .orElseThrow(()->new ResourceNotFoundException("Claim not found"));
        if(c.getAssignedStaffId()==null||!c.getAssignedStaffId().equals(staffId))
            throw new ForbiddenException("Claim is not assigned to current staff member");
        return view(c);
    }
    private ClaimAccessView view(Claim c){return new ClaimAccessView(c.getClaimId(),c.getClaimantId(),c.getStatus(),c.getMarket(),c.getAssignedStaffId());}
}
