package com.chubb.apac.claims.modulith.common.exception;
import org.junit.jupiter.api.Test;import static org.assertj.core.api.Assertions.assertThat;
class GlobalExceptionHandlerTest {@Test void mapsNotFound(){var r=new GlobalExceptionHandler().notFound(new ResourceNotFoundException("missing"));assertThat(r.getStatusCode().value()).isEqualTo(404);assertThat(r.getBody().success()).isFalse();}}
