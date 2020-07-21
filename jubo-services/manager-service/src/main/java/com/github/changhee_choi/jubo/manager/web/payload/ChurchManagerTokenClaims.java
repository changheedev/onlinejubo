package com.github.changhee_choi.jubo.manager.web.payload;

import com.github.changhee_choi.jubo.core.domain.user.UserTokenClaims;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

/**
 * @author Changhee Choi
 * @since 17/07/2020
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ChurchManagerTokenClaims extends UserTokenClaims {

    private UUID churchId;

    @Builder
    public ChurchManagerTokenClaims(Long id, String name, List<String> roles, UUID churchId) {
        super(id, name, roles);
        this.churchId = churchId;
    }
}
