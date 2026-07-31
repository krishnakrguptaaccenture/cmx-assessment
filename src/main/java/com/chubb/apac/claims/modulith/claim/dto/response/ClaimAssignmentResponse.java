package com.chubb.apac.claims.modulith.claim.dto.response;
import java.time.Instant;
public record ClaimAssignmentResponse(String claimId,String assignedStaffId,String assignedStaffName,Instant assignedAt) {}
