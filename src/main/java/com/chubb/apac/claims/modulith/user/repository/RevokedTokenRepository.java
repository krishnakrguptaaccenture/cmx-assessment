package com.chubb.apac.claims.modulith.user.repository;
import com.chubb.apac.claims.modulith.user.model.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RevokedTokenRepository extends JpaRepository<RevokedToken,String> {}
