package com.github.changhee_choi.jubo.core.domain.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Changhee Choi
 * @since 02/07/2020
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class UserTokenClaims {
    private Long id;
    private String name;
    private List<String> roles;
}
