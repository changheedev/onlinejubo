package com.github.changhee_choi.jubo.core.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Changhee Choi
 * @since 20/06/2020
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
