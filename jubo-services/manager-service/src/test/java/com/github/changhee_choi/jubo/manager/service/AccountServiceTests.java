package com.github.changhee_choi.jubo.manager.service;

import com.github.changhee_choi.jubo.core.domain.user.User;
import com.github.changhee_choi.jubo.core.domain.user.UserRepository;
import com.github.changhee_choi.jubo.manager.userdetails.ChurchManagerDetails;
import com.github.changhee_choi.jubo.manager.web.payload.SignUpRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Changhee Choi
 * @since 24/06/2020
 */
@SpringBootTest
class AccountServiceTests {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void teardown() {
        Optional<User> optionalUser = userRepository.findByEmail("test@email.com");
        if(optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
        }
    }

    @Test
    void signUp() {
        SignUpRequest request = SignUpRequest.builder()
                .name("testUser")
                .email("test@email.com")
                .password("password")
                .churchName("My Church")
                .churchMemberNum(20)
                .build();

        ChurchManagerDetails managerDetails = accountService.signUp(request);

        assertThat(managerDetails).isNotNull();
        assertThat(managerDetails.getPassword()).isNotEqualTo("password");
        assertThat(managerDetails.getChurchInfo().getId().toString().length()).isEqualTo(36);
        assertThat(managerDetails.getChurchInfo().getName()).isEqualTo("My Church");
        assertThat(managerDetails.getChurchInfo().getMemberNum()).isEqualTo(20);
        assertThat(managerDetails.isServiceAllowed()).isFalse();
        assertThat(managerDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))).isTrue();
    }

    @Test
    void signUpWhenDuplicatedEmailThatThrowDuplicateEmailException() {
        SignUpRequest request = SignUpRequest.builder()
                .name("testUser")
                .email("test@email.com")
                .password("password")
                .churchName("My Church")
                .churchMemberNum(20)
                .build();

        assertThatThrownBy(() -> {
            accountService.signUp(request);
            accountService.signUp(request);
        }).isInstanceOf(DuplicateEmailException.class);
    }
}