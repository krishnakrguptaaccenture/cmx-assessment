package com.chubb.apac.claims.modulith.incident.repository;
import com.chubb.apac.claims.modulith.incident.model.IncidentParty;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface IncidentPartyRepository extends JpaRepository<IncidentParty,String> { List<IncidentParty> findByIncidentIncidentIdOrderByCreatedAtAsc(String incidentId); }
