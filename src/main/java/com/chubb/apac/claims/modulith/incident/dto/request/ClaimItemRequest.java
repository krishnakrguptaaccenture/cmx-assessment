package com.chubb.apac.claims.modulith.incident.dto.request;
import com.chubb.apac.claims.modulith.incident.model.ClaimItemType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record ClaimItemRequest(@NotNull ClaimItemType itemType,@NotBlank @Size(max=1000) String description,@DecimalMin("0.00") BigDecimal estimatedValue) {}
