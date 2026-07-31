package com.chubb.apac.claims.modulith.user.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name="users", indexes={@Index(name="idx_users_email", columnList="email", unique=true)})
@Getter @Setter @NoArgsConstructor
public class User {
  @Id @Column(length=36) private String id;
  @Column(nullable=false, unique=true, length=254) private String email;
  @Column(nullable=false) private String password;
  @Column(nullable=false, length=150) private String fullName;
  @Column(length=30) private String phoneNumber;
  @Column(nullable=false) private boolean active=true;
  @ElementCollection(fetch=FetchType.EAGER)
  @CollectionTable(name="user_roles", joinColumns=@JoinColumn(name="user_id"))
  @Enumerated(EnumType.STRING) @Column(name="role", nullable=false, length=30)
  private Set<UserRole> roles=new LinkedHashSet<>();
  @ElementCollection(fetch=FetchType.EAGER)
  @CollectionTable(name="user_markets", joinColumns=@JoinColumn(name="user_id"))
  @Enumerated(EnumType.STRING) @Column(name="market", nullable=false, length=2)
  private Set<Market> markets=new LinkedHashSet<>();
  @Column(name="team_id", length=50) private String teamId;
  @CreationTimestamp @Column(nullable=false, updatable=false) private LocalDateTime createdAt;
  @UpdateTimestamp @Column(nullable=false) private LocalDateTime updatedAt;
  @Version private long version;
}
