package com.github.changhee_choi.jubo.manager.service;

import com.github.changhee_choi.jubo.core.userdetails.ChurchManagerDetails;
import com.github.changhee_choi.jubo.manager.web.payload.AuthenticationRequest;

/**
 * @author Changhee Choi
 * @since 27/06/2020
 */
public interface AuthenticationService {
    ChurchManagerDetails authenticate(AuthenticationRequest request);
}
