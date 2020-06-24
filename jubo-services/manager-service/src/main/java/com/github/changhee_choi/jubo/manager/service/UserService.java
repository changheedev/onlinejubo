package com.github.changhee_choi.jubo.manager.service;

import com.github.changhee_choi.jubo.manager.model.web.SignUpRequest;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Changhee Choi
 * @since 24/06/2020
 */
public interface UserService {
    UserDetails signUp(SignUpRequest request);
}
