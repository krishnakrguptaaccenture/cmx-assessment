package com.chubb.apac.claims.modulith.assessment.dto.response;import com.chubb.apac.claims.modulith.assessment.model.ValidationSeverity;
public record ValidationErrorResponse(String code,String message,ValidationSeverity severity) {}
