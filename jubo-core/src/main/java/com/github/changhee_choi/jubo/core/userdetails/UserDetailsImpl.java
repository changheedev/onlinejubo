package com.github.changhee_choi.jubo.core.userdetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.changhee_choi.jubo.core.domain.user.Role;
import com.github.changhee_choi.jubo.core.domain.user.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Changhee Choi
 * @since 24/06/2020
 */
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class UserDetailsImpl implements UserDetails {

    private Long id;

    private String email;

    private String name;

    @JsonIgnore
    private String password;

    private LocalDateTime createdDate;

    private String createdBy;

    private LocalDateTime lastModifiedDate;

    private String lastModifiedBy;

    private Collection<? extends GrantedAuthority> authorities;

    private boolean accountNonLocked;

    private boolean withdraw;

    private boolean emailConfirmed;

    protected UserDetailsImpl(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.password = user.getPassword();
        this.authorities = user.getRoles().stream()
                .map(Role::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        this.createdDate = user.getCreatedDate();
        this.createdBy = user.getCreatedBy();
        this.lastModifiedDate = user.getLastModifiedDate();
        this.lastModifiedBy = user.getLastModifiedBy();
        this.accountNonLocked = user.isAccountNonLocked();
        this.withdraw = user.isWithdraw();
        this.emailConfirmed = user.isEmailConfirmed();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
}
