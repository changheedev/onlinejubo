package com.github.changhee_choi.jubo.core.domain.church;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author Changhee Choi
 * @since 21/06/2020
 */
public interface ChurchRepository extends JpaRepository<Church, UUID> {
}
