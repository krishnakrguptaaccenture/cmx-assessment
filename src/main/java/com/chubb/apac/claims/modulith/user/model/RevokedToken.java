package com.chubb.apac.claims.modulith.user.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
@Entity @Table(name="revoked_tokens", indexes=@Index(name="idx_revoked_tokens_expiry", columnList="expiresAt"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RevokedToken {
  @Id @Column(length=64) private String tokenHash;
  @Column(nullable=false) private Instant expiresAt;
}
