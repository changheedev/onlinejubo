package com.github.changhee_choi.jubo.manager.service;

import com.github.changhee_choi.jubo.core.userdetails.ChurchManagerDetails;
import com.github.changhee_choi.jubo.manager.web.payload.SignUpRequest;

/**
 * @author Changhee Choi
 * @since 24/06/2020
 */
public interface AccountService {
    ChurchManagerDetails signUp(SignUpRequest request);
}
