package com.github.changhee_choi.jubo.manager.service;

import com.github.changhee_choi.jubo.core.domain.jubo.JuboContentDetails;
import com.github.changhee_choi.jubo.manager.web.payload.JuboContentRegistrationPayload;

import java.util.UUID;

/**
 * @author Changhee Choi
 * @since 17/07/2020
 */
public interface JuboContentService {
    JuboContentDetails register(UUID churchId, Long juboId, JuboContentRegistrationPayload juboContentRegistrationPayload);
}
