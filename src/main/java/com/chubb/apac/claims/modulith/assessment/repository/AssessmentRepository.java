package com.chubb.apac.claims.modulith.assessment.repository;
import com.chubb.apac.claims.modulith.assessment.model.*;import com.chubb.apac.claims.modulith.common.enums.Market;import java.util.*;import org.springframework.data.domain.*;import org.springframework.data.jpa.repository.JpaRepository;
public interface AssessmentRepository extends JpaRepository<Assessment,String>{Optional<Assessment> findByClaimId(String claimId);boolean existsByClaimId(String claimId);Page<Assessment> findByStatusAndMarketIn(AssessmentStatus status,Collection<Market> markets,Pageable pageable);}
