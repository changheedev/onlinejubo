package com.github.changhee_choi.jubo.core.domain.jubo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author Changhee Choi
 * @since 21/06/2020
 */
public interface ChurchInfoRepository extends JpaRepository<ChurchInfo, UUID> {
}
