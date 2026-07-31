package com.chubb.apac.claims.modulith.common.config;
import com.chubb.apac.claims.modulith.common.util.CorrelationId;import org.junit.jupiter.api.Test;import org.springframework.mock.web.*;import static org.assertj.core.api.Assertions.assertThat;
class CorrelationIdFilterTest {@Test void createsCorrelationId()throws Exception{var req=new MockHttpServletRequest();var res=new MockHttpServletResponse();new CorrelationIdFilter().doFilter(req,res,(a,b)->{});assertThat(res.getHeader(CorrelationId.HEADER)).isNotBlank();}}
