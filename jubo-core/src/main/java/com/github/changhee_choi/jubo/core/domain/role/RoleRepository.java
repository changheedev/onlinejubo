package com.github.changhee_choi.jubo.core.domain.role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Changhee Choi
 * @since 20/06/2020
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
