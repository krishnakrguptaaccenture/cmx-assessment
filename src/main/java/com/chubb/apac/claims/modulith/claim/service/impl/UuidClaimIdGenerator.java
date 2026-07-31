package com.chubb.apac.claims.modulith.claim.service.impl;
import com.chubb.apac.claims.modulith.claim.service.ClaimIdGenerator;import java.util.UUID;import org.springframework.stereotype.Component;
@Component public class UuidClaimIdGenerator implements ClaimIdGenerator {public String nextClaimId(){return "CLM-"+UUID.randomUUID().toString().toUpperCase();}}
