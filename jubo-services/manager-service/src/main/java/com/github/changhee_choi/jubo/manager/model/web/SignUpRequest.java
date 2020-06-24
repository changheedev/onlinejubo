package com.github.changhee_choi.jubo.manager.model.web;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

/**
 * @author Changhee Choi
 * @since 24/06/2020
 */
@Data
@Builder
public class SignUpRequest {

    @Pattern(regexp = "^[a-zA-Z가-힣0-9-_]{2,20}$")
    private String name;

    @Email
    private String email;

    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[a-zA-Z]).{8,20}$")
    private String password;
}
