package com.chubb.apac.claims.modulith.inforequest.repository;
import com.chubb.apac.claims.modulith.inforequest.model.InformationResponse;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface InformationResponseRepository extends JpaRepository<InformationResponse,String> {
    List<InformationResponse> findByRequestIdOrderBySubmittedAtAsc(String requestId);
}
