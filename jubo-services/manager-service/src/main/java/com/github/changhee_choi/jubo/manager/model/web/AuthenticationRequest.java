package com.github.changhee_choi.jubo.manager.model.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Changhee Choi
 * @since 27/06/2020
 */
@Data
@Builder
@AllArgsConstructor
public class AuthenticationRequest {
    private String username;
    private String password;
}
