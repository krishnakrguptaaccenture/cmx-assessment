package com.chubb.apac.claims.modulith.common.config;
import com.chubb.apac.claims.modulith.common.security.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration @EnableMethodSecurity @EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {
 @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
 @Bean SecurityFilterChain filterChain(HttpSecurity h,JwtAuthenticationFilter f,RestAuthenticationEntryPoint ep,RestAccessDeniedHandler dh)throws Exception{return h.csrf(c->c.ignoringRequestMatchers("/h2-console/**").disable()).headers(x->x.frameOptions(y->y.sameOrigin())).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).exceptionHandling(e->e.authenticationEntryPoint(ep).accessDeniedHandler(dh)).authorizeHttpRequests(a->a.requestMatchers("/api/v1/auth/register","/api/v1/auth/login","/h2-console/**","/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html","/actuator/health").permitAll().anyRequest().authenticated()).addFilterBefore(f,UsernamePasswordAuthenticationFilter.class).build();}
}
