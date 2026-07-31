package com.chubb.apac.claims.modulith.inforequest.dto.request;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
public record CreateInformationRequest(
        @NotEmpty List<@NotBlank @Size(max=250) String> requestedFields,
        @NotNull @FutureOrPresent LocalDate dueDate,
        @Size(max=2000) String instructions) {
    public CreateInformationRequest {
        requestedFields = requestedFields == null ? null : List.copyOf(requestedFields);
    }
}
