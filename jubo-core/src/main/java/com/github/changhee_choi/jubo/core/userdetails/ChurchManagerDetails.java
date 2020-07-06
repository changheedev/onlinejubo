package com.github.changhee_choi.jubo.core.userdetails;

import com.github.changhee_choi.jubo.core.domain.user.ChurchManager;
import com.github.changhee_choi.jubo.core.dto.ChurchDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Changhee Choi
 * @since 27/06/2020
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class ChurchManagerDetails extends UserDetailsImpl {

    private ChurchDTO churchInfo;
    private boolean serviceAllowed;

    public ChurchManagerDetails(ChurchManager manager) {
        super(manager);
        this.churchInfo = manager.getChurch().toDTO();
        this.serviceAllowed = manager.isServiceAllowed();
    }
}
