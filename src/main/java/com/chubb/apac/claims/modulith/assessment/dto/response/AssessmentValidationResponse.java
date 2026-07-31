package com.chubb.apac.claims.modulith.assessment.dto.response;import java.util.List;
public record AssessmentValidationResponse(boolean isValid,List<ValidationErrorResponse> validationErrors) {}
