package com.chubb.apac.claims.modulith.claim.repository;
import com.chubb.apac.claims.modulith.claim.model.ClaimAssignment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ClaimAssignmentRepository extends JpaRepository<ClaimAssignment,String>{Optional<ClaimAssignment> findFirstByClaimIdAndUnassignedAtIsNullOrderByAssignedAtDesc(String claimId);}
