package com.github.changhee_choi.jubo.manager.service;

import com.github.changhee_choi.jubo.core.domain.church.Church;
import com.github.changhee_choi.jubo.core.domain.church.ChurchRepository;
import com.github.changhee_choi.jubo.core.domain.user.ChurchManager;
import com.github.changhee_choi.jubo.core.domain.user.User;
import com.github.changhee_choi.jubo.core.domain.user.UserRepository;
import com.github.changhee_choi.jubo.core.userdetails.ChurchManagerDetails;
import com.github.changhee_choi.jubo.manager.model.web.AuthenticationRequest;
import com.github.changhee_choi.jubo.manager.model.web.SignUpRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Changhee Choi
 * @since 27/06/2020
 */
@SpringBootTest
class AuthenticationServiceTests {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChurchRepository churchRepository;

    @Test
    void authenticate() {
        SignUpRequest request = SignUpRequest.builder()
                .email("test@email.com").password("password").name("TestUser")
                .churchName("My Church").churchMemberNum(20).build();

        accountService.signUp(request);

        ChurchManagerDetails managerDetails = authenticationService.authenticate(
                AuthenticationRequest.builder().username(request.getEmail()).password(request.getPassword()).build());

        assertThat(managerDetails.getChurchInfo()).isNotNull();
        assertThat(managerDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))).isTrue();
    }

    @Test
    @Transactional
    void authenticate를_실행할때_User의_Lock_상태가_true이면_LockedException이_던져진다() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        Church church = Church.builder().name("My Church").memberNum(20).build();
        churchRepository.save(church);

        User user = ChurchManager.builder().name("test_user").email("test2@email.com")
                .password(passwordEncoder.encode("password")).church(church).build();

        //account lock
        user.lockAccount();
        userRepository.save(user);

        assertThatThrownBy(() ->
                authenticationService.authenticate(
                        AuthenticationRequest.builder()
                                .username(user.getEmail())
                                .password("password").build()
                )
        ).isInstanceOf(LockedException.class);
    }

    @Test
    void authenticate를_실행할때_Password가_일치하지_않으면_BadCredentialsException이_던져진다() {
        SignUpRequest request = SignUpRequest.builder()
                .email("test3@email.com").password("password").name("TestUser")
                .churchName("My Church").churchMemberNum(20).build();

        accountService.signUp(request);

        assertThatThrownBy(() ->
                authenticationService.authenticate(
                        AuthenticationRequest.builder()
                                .username(request.getEmail())
                                .password("wrong_password").build()
                )
        ).isInstanceOf(BadCredentialsException.class);
    }
}