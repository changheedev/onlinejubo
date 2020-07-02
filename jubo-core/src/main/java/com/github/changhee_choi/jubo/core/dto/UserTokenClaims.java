package com.github.changhee_choi.jubo.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Changhee Choi
 * @since 02/07/2020
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTokenClaims {
    private Long id;
    private String name;
    private List<String> roles;
}
