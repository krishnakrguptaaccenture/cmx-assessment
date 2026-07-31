package com.chubb.apac.claims.modulith.incident.controller;

import com.chubb.apac.claims.modulith.common.enums.Market;
import com.chubb.apac.claims.modulith.common.enums.UserRole;
import com.chubb.apac.claims.modulith.common.security.CurrentUser;
import com.chubb.apac.claims.modulith.incident.dto.request.CreateIncidentRequest;
import com.chubb.apac.claims.modulith.incident.dto.response.IncidentResponse;
import com.chubb.apac.claims.modulith.incident.model.IncidentStatus;
import com.chubb.apac.claims.modulith.incident.model.IncidentType;
import com.chubb.apac.claims.modulith.incident.service.IncidentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class IncidentControllerTest {
    private IncidentService service;
    private MockMvc mvc;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        service = mock(IncidentService.class);
        CurrentUser currentUser = new CurrentUser(
                "U1", "a@b.com", Set.of(UserRole.CLAIMANT), Set.of(Market.SG), null);
        HandlerMethodArgumentResolver currentUserResolver = new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return parameter.hasParameterAnnotation(AuthenticationPrincipal.class)
                        && parameter.getParameterType().equals(CurrentUser.class);
            }
            @Override
            public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                    NativeWebRequest webRequest,
                    org.springframework.web.bind.support.WebDataBinderFactory binderFactory) {
                return currentUser;
            }
        };
        mvc = MockMvcBuilders.standaloneSetup(new IncidentController(service))
                .setCustomArgumentResolvers(currentUserResolver)
                .build();
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    void reportReturns201() throws Exception {
        var response = new IncidentResponse("INC-1", "U1", Instant.EPOCH,
                IncidentType.MOTOR_ACCIDENT, "L", "D", Market.SG,
                IncidentStatus.REPORTED, List.of(), null, null);
        when(service.report(any(), any(), any())).thenReturn(response);
        var request = new CreateIncidentRequest(Instant.now(),
                IncidentType.MOTOR_ACCIDENT, "L", "D", Market.SG, List.of());

        mvc.perform(post("/api/v1/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.incidentId").value("INC-1"));
    }
}
