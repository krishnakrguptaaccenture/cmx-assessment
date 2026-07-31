package com.chubb.apac.claims.modulith.inforequest.dto.response;
import java.time.Instant;
public record InformationResponseSummary(String responseId,String response,Instant submittedAt) {}
