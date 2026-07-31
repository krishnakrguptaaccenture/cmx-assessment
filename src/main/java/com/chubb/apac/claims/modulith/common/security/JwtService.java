package com.chubb.apac.claims.modulith.common.security;
import com.chubb.apac.claims.modulith.common.enums.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;
@Service
public class JwtService {
 private final SecretKey key; private final long expiration;
 public JwtService(JwtProperties p){key=Keys.hmacShaKeyFor(p.secret().getBytes(StandardCharsets.UTF_8));expiration=p.expiration();}
 public String generate(JwtClaims c){Instant now=Instant.now();return Jwts.builder().subject(c.userId()).claim("email",c.email()).claim("roles",c.roles().stream().map(Enum::name).toList()).claim("markets",c.markets().stream().map(Enum::name).toList()).claim("teamId",c.teamId()).issuedAt(Date.from(now)).expiration(Date.from(now.plusMillis(expiration))).signWith(key).compact();}
 public CurrentUser parse(String token){Claims c=Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();return new CurrentUser(c.getSubject(),c.get("email",String.class),enumSet(c.get("roles",List.class),UserRole.class),enumSet(c.get("markets",List.class),Market.class),c.get("teamId",String.class));}
 public Instant expiry(String token){return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getExpiration().toInstant();}
 private static <E extends Enum<E>> Set<E> enumSet(List<?> values,Class<E> type){if(values==null)return Set.of();Set<E> out=new HashSet<>();for(Object v:values)out.add(Enum.valueOf(type,String.valueOf(v)));return Set.copyOf(out);}
}
