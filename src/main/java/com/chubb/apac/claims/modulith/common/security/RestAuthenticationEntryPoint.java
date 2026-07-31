package com.chubb.apac.claims.modulith.common.security;
import com.chubb.apac.claims.modulith.common.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import org.springframework.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {private final ObjectMapper mapper;public RestAuthenticationEntryPoint(ObjectMapper mapper){this.mapper=mapper;}public void commence(HttpServletRequest req,HttpServletResponse res,AuthenticationException e)throws IOException{res.setStatus(401);res.setContentType(MediaType.APPLICATION_JSON_VALUE);mapper.writeValue(res.getOutputStream(),ApiResponse.failure(new ErrorDetail("UNAUTHORISED","Authentication is required",List.of())));}}
