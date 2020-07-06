package com.github.changhee_choi.jubo.core.domain;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * @author Changhee Choi
 * @since 20/06/2020
 */
public class TestAuditorAware implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        return Optional.of(1L);
    }
}