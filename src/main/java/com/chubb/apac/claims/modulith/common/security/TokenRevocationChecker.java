package com.chubb.apac.claims.modulith.common.security;
@FunctionalInterface public interface TokenRevocationChecker { boolean isRevoked(String token); }
