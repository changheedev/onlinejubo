package com.github.changhee_choi.jubo.manager.userdetails;

import com.github.changhee_choi.jubo.core.domain.user.ChurchManager;
import com.github.changhee_choi.jubo.core.domain.church.ChurchDetails;
import com.github.changhee_choi.jubo.core.userdetails.UserDetailsImpl;
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

    private ChurchDetails churchInfo;
    private boolean serviceAllowed;

    public ChurchManagerDetails(ChurchManager manager) {
        super(manager);
        this.churchInfo = ChurchDetails.of(manager.getChurch());
        this.serviceAllowed = manager.isServiceAllowed();
    }
}
