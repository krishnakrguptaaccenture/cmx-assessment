package com.chubb.apac.claims.modulith.claim.repository;
import com.chubb.apac.claims.modulith.claim.model.Claim;
import com.chubb.apac.claims.modulith.common.enums.*;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
public interface ClaimRepository extends JpaRepository<Claim,String>,JpaSpecificationExecutor<Claim> {
 Optional<Claim> findByClaimIdAndClaimantId(String claimId,String claimantId);
 Optional<Claim> findByClaimIdAndMarketIn(String claimId,java.util.Collection<Market> markets);
 Optional<Claim> findByIncidentId(String incidentId);
 boolean existsByIncidentId(String incidentId);
 Page<Claim> findByClaimantIdAndStatus(String claimantId,ClaimStatus status,Pageable pageable);
 Page<Claim> findByClaimantId(String claimantId,Pageable pageable);
 @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select c from Claim c where c.claimId=:claimId and c.market in :markets")
 Optional<Claim> findForUpdate(@Param("claimId") String claimId,@Param("markets") java.util.Collection<Market> markets);
}
