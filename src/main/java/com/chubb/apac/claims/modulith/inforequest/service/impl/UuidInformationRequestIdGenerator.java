package com.chubb.apac.claims.modulith.inforequest.service.impl;
import com.chubb.apac.claims.modulith.inforequest.service.InformationRequestIdGenerator;
import java.util.UUID;import org.springframework.stereotype.Component;
@Component public class UuidInformationRequestIdGenerator implements InformationRequestIdGenerator {
 public String nextRequestId(){return "IRQ-"+UUID.randomUUID().toString().toUpperCase();}
 public String nextResponseId(){return "IRS-"+UUID.randomUUID().toString().toUpperCase();}
}
