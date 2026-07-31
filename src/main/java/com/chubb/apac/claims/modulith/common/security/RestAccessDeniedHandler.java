package com.chubb.apac.claims.modulith.common.security;
import com.chubb.apac.claims.modulith.common.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {private final ObjectMapper mapper;public RestAccessDeniedHandler(ObjectMapper mapper){this.mapper=mapper;}public void handle(HttpServletRequest req,HttpServletResponse res,AccessDeniedException e)throws IOException{res.setStatus(403);res.setContentType(MediaType.APPLICATION_JSON_VALUE);mapper.writeValue(res.getOutputStream(),ApiResponse.failure(new ErrorDetail("FORBIDDEN","Access is denied",List.of())));}}
