package com.github.changhee_choi.jubo.manager.service;

import com.github.changhee_choi.jubo.core.domain.jubo.JuboDetails;
import com.github.changhee_choi.jubo.manager.web.payload.JuboRequest;

import java.util.UUID;

/**
 * @author Changhee Choi
 * @since 06/07/2020
 */
public interface JuboService {
    JuboDetails register(UUID churchId, JuboRequest payload);
}
