package com.chubb.apac.claims.modulith.user.repository;
import com.chubb.apac.claims.modulith.user.model.User;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface UserRepository extends JpaRepository<User,String> {
  Optional<User> findByEmailIgnoreCase(String email);
  boolean existsByEmailIgnoreCase(String email);
  Page<User> findDistinctByRolesIn(java.util.Collection<com.chubb.apac.claims.modulith.user.model.UserRole> roles, Pageable pageable);
}
