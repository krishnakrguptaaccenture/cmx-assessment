package com.chubb.apac.claims.modulith.common.security;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
@Component @ConditionalOnMissingBean(TokenRevocationChecker.class)
public class NoOpTokenRevocationChecker implements TokenRevocationChecker
{public boolean isRevoked(String token){return false;}}
