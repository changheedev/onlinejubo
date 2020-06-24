package com.github.changhee_choi.jubo.core.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Changhee Choi
 * @since 23/06/2020
 */
public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {
}
