package com.chubb.apac.claims.modulith.incident.repository;
import com.chubb.apac.claims.modulith.incident.model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface IncidentRepository extends JpaRepository<Incident,String> {
 Optional<Incident> findByIncidentIdAndClaimantId(String incidentId,String claimantId);
 boolean existsByIncidentId(String incidentId);
}
