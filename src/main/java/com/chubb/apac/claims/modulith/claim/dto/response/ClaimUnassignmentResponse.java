package com.chubb.apac.claims.modulith.claim.dto.response;
import java.time.Instant;
public record ClaimUnassignmentResponse(String claimId,Instant unassignedAt) {}
