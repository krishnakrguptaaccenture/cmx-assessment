package com.chubb.apac.claims.modulith.integration;

import com.chubb.apac.claims.modulith.assessment.repository.AssessmentRepository;
import com.chubb.apac.claims.modulith.claim.repository.ClaimRepository;
import com.chubb.apac.claims.modulith.config.repository.MarketConfigurationRepository;
import com.chubb.apac.claims.modulith.config.repository.ProductConfigurationRepository;
import com.chubb.apac.claims.modulith.inforequest.repository.InformationRequestRepository;
import com.chubb.apac.claims.modulith.notification.repository.NotificationLogRepository;
import com.chubb.apac.claims.modulith.notification.repository.NotificationTemplateRepository;
import com.chubb.apac.claims.modulith.user.repository.RevokedTokenRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplicationModulesIT {

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private InformationRequestRepository informationRequestRepository;

    @Autowired
    private NotificationLogRepository notificationLogRepository;

    @Autowired
    private NotificationTemplateRepository notificationTemplateRepository;

    @Autowired
    private MarketConfigurationRepository marketConfigurationRepository;

    @Autowired
    private ProductConfigurationRepository productConfigurationRepository;

    @Autowired
    private RevokedTokenRepository revokedTokenRepository;

    @Test
    void applicationContextLoadsAndAllModulesAreWired() {

        assertThat(claimRepository).isNotNull();

        assertThat(assessmentRepository).isNotNull();

        assertThat(informationRequestRepository).isNotNull();

        assertThat(notificationLogRepository).isNotNull();

        assertThat(notificationTemplateRepository).isNotNull();

        assertThat(marketConfigurationRepository).isNotNull();

        assertThat(productConfigurationRepository).isNotNull();

        assertThat(revokedTokenRepository).isNotNull();
    }

    @Test
    void configurationSeedDataLoaded() {

        assertThat(marketConfigurationRepository.findAll())
                .isNotEmpty();

        assertThat(productConfigurationRepository.findAll())
                .isNotEmpty();
    }

    @Test
    void notificationTemplatesLoaded() {

        assertThat(notificationTemplateRepository.findAll())
                .isNotEmpty();
    }
}