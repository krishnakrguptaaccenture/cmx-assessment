package com.chubb.apac.claims.modulith.user.service;
import java.time.Instant;
public interface TokenRevocationService { void revoke(String token,Instant expiresAt); boolean isRevoked(String token); }
