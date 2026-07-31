package com.chubb.apac.claims.modulith.common.config;
import com.chubb.apac.claims.modulith.common.util.CorrelationId;
import jakarta.servlet.*;import jakarta.servlet.http.*;import java.io.IOException;import java.util.UUID;
import org.slf4j.MDC;import org.springframework.stereotype.Component;import org.springframework.web.filter.OncePerRequestFilter;
@Component
public class CorrelationIdFilter extends OncePerRequestFilter {protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)throws ServletException,IOException{String id=req.getHeader(CorrelationId.HEADER);if(id==null||id.isBlank())id=UUID.randomUUID().toString();MDC.put("correlationId",id);res.setHeader(CorrelationId.HEADER,id);try{chain.doFilter(req,res);}finally{MDC.remove("correlationId");}}}
