package com.chubb.apac.claims.modulith.inforequest.repository;
import com.chubb.apac.claims.modulith.inforequest.model.*;
import jakarta.persistence.LockModeType;
import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
public interface InformationRequestRepository extends JpaRepository<InformationRequest,String> {
    List<InformationRequest> findByClaimIdOrderByCreatedAtDesc(String claimId);
    Optional<InformationRequest> findByRequestIdAndClaimId(String requestId,String claimId);
    boolean existsByClaimIdAndStatusIn(String claimId,Collection<InformationRequestStatus> statuses);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from InformationRequest r where r.requestId=:requestId and r.claimId=:claimId")
    Optional<InformationRequest> findForUpdate(@Param("requestId") String requestId,@Param("claimId") String claimId);
}
