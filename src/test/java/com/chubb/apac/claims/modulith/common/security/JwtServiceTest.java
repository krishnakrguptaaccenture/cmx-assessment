package com.chubb.apac.claims.modulith.common.security;
import com.chubb.apac.claims.modulith.common.enums.*;import org.junit.jupiter.api.Test;import java.util.Set;import static org.assertj.core.api.Assertions.assertThat;
class JwtServiceTest {@Test void roundTrip(){var s=new JwtService(new JwtProperties("01234567890123456789012345678901",60000));String t=s.generate(new JwtClaims("u1","a@b.com",Set.of(UserRole.CLAIMANT),Set.of(Market.SG),null));var u=s.parse(t);assertThat(u.userId()).isEqualTo("u1");assertThat(u.roles()).containsExactly(UserRole.CLAIMANT);}}
