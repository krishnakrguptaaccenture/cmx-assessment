package com.chubb.apac.claims.modulith.claim.repository;
import com.chubb.apac.claims.modulith.claim.model.ClaimStatusHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ClaimStatusHistoryRepository extends JpaRepository<ClaimStatusHistory,String>{List<ClaimStatusHistory> findByClaimIdOrderByChangedAtAsc(String claimId);}
