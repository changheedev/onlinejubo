package com.github.changhee_choi.jubo.core.config;

import com.github.changhee_choi.jubo.core.domain.SecurityAuditorAware;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Changhee Choi
 * @since 24/06/2020
 */
@Configuration
@EnableJpaAuditing
@EntityScan(basePackages = {"com.github.changhee_choi.jubo.**.domain"})
@EnableJpaRepositories(basePackages = {"com.github.changhee_choi.jubo.**.domain"})
public class DomainConfig {

    @Bean
    public AuditorAware auditorAware() {
        return new SecurityAuditorAware();
    }
}
