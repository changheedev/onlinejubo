package com.github.changhee_choi.jubo.core.domain.attachment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author Changhee Choi
 * @since 22/06/2020
 */
public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {
}
