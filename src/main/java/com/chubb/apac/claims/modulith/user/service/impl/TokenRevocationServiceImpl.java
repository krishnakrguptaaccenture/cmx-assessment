package com.chubb.apac.claims.modulith.user.service.impl;
import com.chubb.apac.claims.modulith.user.model.RevokedToken;
import com.chubb.apac.claims.modulith.user.repository.RevokedTokenRepository;
import com.chubb.apac.claims.modulith.user.service.TokenRevocationService;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;import java.security.*;import java.time.Instant;import java.util.HexFormat;
@Service
public class TokenRevocationServiceImpl implements TokenRevocationService {
 private final RevokedTokenRepository repo; public TokenRevocationServiceImpl(RevokedTokenRepository repo){this.repo=repo;}
 private String hash(String t){try{return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(t.getBytes(StandardCharsets.UTF_8)));}catch(NoSuchAlgorithmException e){throw new IllegalStateException(e);}}
 public void revoke(String t,Instant e){repo.save(new RevokedToken(hash(t),e));}
 public boolean isRevoked(String t){return repo.existsById(hash(t));}
}
