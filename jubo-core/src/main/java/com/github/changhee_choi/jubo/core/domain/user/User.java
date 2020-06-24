package com.github.changhee_choi.jubo.core.domain.user;

import com.github.changhee_choi.jubo.core.domain.BaseEntity;
import com.github.changhee_choi.jubo.core.domain.jubo.Church;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Changhee Choi
 * @since 20/06/2020
 */
@Entity
@Table(name = "oj_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean withdraw;

    @Column(nullable = false)
    private boolean accountLocked;

    @Column(nullable = false)
    private boolean emailConfirmed;

    @Column(nullable = false)
    private boolean serviceApproved;

    @ManyToMany
    @JoinTable(name = "oj_user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "church_id")
    private Church church;

    @Builder
    public User(String name, String email, String password, boolean serviceApproved) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.serviceApproved = serviceApproved;
        this.withdraw = false;
        this.accountLocked = false;
        this.emailConfirmed = false;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void unlockAccount() {
        this.accountLocked = false;
    }

    public void lockAccount() {
        this.accountLocked = true;
    }

    public void withdrawAccount() {
        this.withdraw = true;
    }

    public void confirmAccountEmail() {
        this.emailConfirmed = true;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void setChurch(Church church) {
        if (this.church != null) throw new IllegalStateException("등록된 교회 정보가 존재합니다.");
        this.church = church;
    }

    public void approveService() {
        this.serviceApproved = true;
    }
}
