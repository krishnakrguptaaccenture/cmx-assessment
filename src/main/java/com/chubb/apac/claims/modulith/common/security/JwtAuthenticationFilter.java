package com.chubb.apac.claims.modulith.common.security;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
 private final JwtService jwt;
 private final TokenRevocationChecker revocations;
 public JwtAuthenticationFilter(JwtService jwt,TokenRevocationChecker revocations){this.jwt=jwt;this.revocations=revocations;}
 @Override protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)throws ServletException,IOException{
  String h=req.getHeader("Authorization");
  if(h!=null&&h.startsWith("Bearer ")){String token=h.substring(7);try{if(!revocations.isRevoked(token)){CurrentUser u=jwt.parse(token);var authorities=u.roles().stream().map(r->new SimpleGrantedAuthority("ROLE_"+r.name())).toList();SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(u,token,authorities));}}catch(JwtException|IllegalArgumentException e){SecurityContextHolder.clearContext();}}
  chain.doFilter(req,res);
 }
}
