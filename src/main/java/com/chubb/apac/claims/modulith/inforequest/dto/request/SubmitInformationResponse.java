package com.chubb.apac.claims.modulith.inforequest.dto.request;
import jakarta.validation.constraints.*;
public record SubmitInformationResponse(@NotBlank @Size(max=8000) String response) {}
