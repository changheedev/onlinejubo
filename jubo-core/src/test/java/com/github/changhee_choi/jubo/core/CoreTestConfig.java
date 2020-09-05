package com.github.changhee_choi.jubo.core;

import com.github.changhee_choi.jubo.core.domain.TestAuditorAware;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;

/**
 * @author Changhee Choi
 * @since 20/06/2020
 */
@TestConfiguration
public class CoreTestConfig {
    @Bean
    public AuditorAware auditorAware() {
        return new TestAuditorAware();
    }
}
