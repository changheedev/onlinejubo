package com.github.changhee_choi.jubo.manager.model.web;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author Changhee Choi
 * @since 24/06/2020
 */
@Data
@Builder
public class SignUpRequest {

    @NotNull
    @Pattern(regexp = "^[a-zA-Z가-힣0-9-_]{2,20}$")
    private String name;

    @NotNull
    @Email(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?")
    private String email;

    @NotNull
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[a-zA-Z]).{8,20}$")
    private String password;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z가-힣\\ ]{2,50}$")
    private String churchName;

    @NotNull
    private Integer churchMemberNum;
}
