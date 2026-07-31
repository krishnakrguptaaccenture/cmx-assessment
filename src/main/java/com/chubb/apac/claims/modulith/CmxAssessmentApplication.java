package com.chubb.apac.claims.modulith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * CMX Assessment - Chubb APAC Claims Processing System.
 *
 * Root Spring Boot application for the modular monolith.
 * All modules are discovered through component scanning under:
 * com.chubb.apac.claims.modulith
 */
@SpringBootApplication
@EnableScheduling
public class CmxAssessmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmxAssessmentApplication.class, args);
    }
}
