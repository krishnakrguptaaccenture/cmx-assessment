package com.chubb.apac.claims.modulith.inforequest.dto.response;
import java.time.Instant;
public record InformationResponseResponse(String responseId,String requestId,String claimId,String response,Instant submittedAt) {}
