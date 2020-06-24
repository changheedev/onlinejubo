package com.github.changhee_choi.jubo.manager.service;

import com.github.changhee_choi.jubo.core.domain.user.Role;
import com.github.changhee_choi.jubo.core.domain.user.RoleRepository;
import com.github.changhee_choi.jubo.core.domain.user.UserDetailsImpl;
import com.github.changhee_choi.jubo.core.domain.user.UserRepository;
import com.github.changhee_choi.jubo.manager.model.web.SignUpRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Changhee Choi
 * @since 24/06/2020
 */
@SpringBootTest
class UserServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        Role role = new Role("ROLE_USER");
        roleRepository.save(role);
    }

    @AfterEach
    void teardown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void signUp() {
        SignUpRequest request = SignUpRequest.builder()
                .name("testUser")
                .email("test@email.com")
                .password("password")
                .build();

        UserDetailsImpl userDetails = (UserDetailsImpl) userService.signUp(request);

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getPassword()).isNotEqualTo("password");
        assertThat(userDetails.getCreatedBy()).isEqualTo("Server");
        assertThat(userDetails.getLastModifiedBy()).isEqualTo("Server");
        assertThat(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))).isTrue();
    }

    @Test
    void signUpWhenDuplicatedEmailThatThrowDuplicateEmailException() {
        SignUpRequest request = SignUpRequest.builder()
                .name("testUser")
                .email("test@email.com")
                .password("password")
                .build();

        assertThatThrownBy(() -> {
            userService.signUp(request);
            userService.signUp(request);
        }).isInstanceOf(DuplicateEmailException.class);
    }
}