package com.chubb.apac.claims.modulith.assessment.dto.request;import jakarta.validation.constraints.*;
public record DecisionRequest(@NotBlank @Size(max=4000) String reason) {}
