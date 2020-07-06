package com.github.changhee_choi.jubo.core.domain;

import com.github.changhee_choi.jubo.core.dto.UserTokenClaims;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * @author Changhee Choi
 * @since 24/06/2020
 */
public class SecurityAuditorAware implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserTokenClaims)) {
            return Optional.empty();
        }
        return Optional.of(((UserTokenClaims) authentication.getPrincipal()).getId());
    }
}

