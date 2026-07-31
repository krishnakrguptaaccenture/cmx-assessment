package com.chubb.apac.claims.modulith.inforequest.dto.response;
import com.chubb.apac.claims.modulith.inforequest.model.InformationRequestStatus;
import java.time.*;
import java.util.List;
public record InformationRequestResponse(String requestId,String claimId,List<String> requestedFields,
        LocalDate dueDate,InformationRequestStatus status,String instructions,LocalDateTime createdAt,
        String createdBy,List<InformationResponseSummary> responses) {}
