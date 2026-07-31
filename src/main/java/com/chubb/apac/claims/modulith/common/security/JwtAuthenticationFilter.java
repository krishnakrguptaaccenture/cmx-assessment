package com.chubb.apac.claims.modulith.common.security;
import com.chubb.apac.claims.modulith.user.service.TokenRevocationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;import java.util.*;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
 private final JwtService jwt; private final TokenRevocationService revocations;
 public JwtAuthenticationFilter(JwtService jwt,TokenRevocationService revocations){this.jwt=jwt;this.revocations=revocations;}
 @Override protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)throws ServletException,IOException{
  String h=req.getHeader("Authorization");
  if(h!=null&&h.startsWith("Bearer ")){String t=h.substring(7);try{if(!revocations.isRevoked(t)){Claims c=jwt.parse(t);List<String> roles=c.get("roles",List.class);Set<String> markets=new HashSet<>(Optional.ofNullable(c.get("markets",List.class)).orElse(List.of()));CurrentUser p=new CurrentUser(c.getSubject(),c.get("email",String.class),new HashSet<>(roles),markets,c.get("teamId",String.class));var a=roles.stream().map(r->new SimpleGrantedAuthority("ROLE_"+r)).toList();SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(p,t,a));}}catch(JwtException|IllegalArgumentException ignored){SecurityContextHolder.clearContext();}}
  chain.doFilter(req,res);
 }
}
