package com.chubb.apac.claims.modulith.common.dto;
import org.junit.jupiter.api.Test;import static org.assertj.core.api.Assertions.assertThat;
class ApiResponseTest {@Test void successEnvelope(){var r=ApiResponse.success("ok");assertThat(r.success()).isTrue();assertThat(r.data()).isEqualTo("ok");assertThat(r.error()).isNull();assertThat(r.timestamp()).isNotNull();}}
