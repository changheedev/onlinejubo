package com.github.changhee_choi.jubo.manager.service;

import com.github.changhee_choi.jubo.core.domain.user.User;
import com.github.changhee_choi.jubo.core.domain.user.UserRepository;
import com.github.changhee_choi.jubo.core.userdetails.ChurchManagerDetails;
import com.github.changhee_choi.jubo.manager.domain.test.TestUserType;
import com.github.changhee_choi.jubo.manager.model.web.SignUpRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Changhee Choi
 * @since 27/06/2020
 */
@SpringBootTest
@Import(TestUserType.class)
class UserDetailsServiceImplTests {

    @Autowired
    private AccountService accountService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
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
    void loadByUsername() {
        SignUpRequest request = SignUpRequest.builder()
                .name("testUser")
                .email("test@email.com")
                .password("password")
                .churchName("My Church")
                .churchMemberNum(20)
                .build();

        accountService.signUp(request);

        ChurchManagerDetails managerDetails = userDetailsService.loadUserByUsername(request.getEmail());
    }

    @Test
    void loadByUsername에서_입력된_이메일로_가입된_회원정보가_없을때_UsernameNotFoundException이_던져진다() {
                assertThatThrownBy(() -> userDetailsService.loadUserByUsername("non-registered@email.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("해당 이메일로 가입된 회원 정보를 찾을 수 없습니다.");
    }

    @Test
    void loadByUsername에서_유저_타입이_ChurchManager_가_아닐때_UsernameNotFoundException이_던져진다() {
        User user = new TestUserType("TestUser", "test@email.com", "password", "testProp");
        userRepository.save(user);

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(user.getEmail()))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("ChurchManager 타입의 회원이 아닙니다.");
    }
}