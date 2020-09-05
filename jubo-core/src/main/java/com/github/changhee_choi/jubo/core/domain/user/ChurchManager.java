package com.github.changhee_choi.jubo.core.domain.user;

import com.github.changhee_choi.jubo.core.domain.church.Church;
import lombok.*;

import javax.persistence.*;

/**
 * @author Changhee Choi
 * @since 27/06/2020
 */

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "oj_church_manager")
@ToString(callSuper = true)
@DiscriminatorValue("M")
public class ChurchManager extends User {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "church_id", nullable = false)
    private Church church;

    @Column(nullable = false)
    private boolean serviceAllowed;

    @Builder
    public ChurchManager(String name, String email, String password, Church church) {
        super(name, email, password);
        this.church = church;
        this.serviceAllowed = false;
    }

    public void allowService() {
        this.serviceAllowed = true;
    }

    public void lockService() {
        this.serviceAllowed = false;
    }
}

