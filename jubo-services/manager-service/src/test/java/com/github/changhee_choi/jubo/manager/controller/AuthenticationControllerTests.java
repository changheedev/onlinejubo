package com.github.changhee_choi.jubo.manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.changhee_choi.jubo.core.domain.user.User;
import com.github.changhee_choi.jubo.core.domain.user.UserRepository;
import com.github.changhee_choi.jubo.manager.model.web.AuthenticationRequest;
import com.github.changhee_choi.jubo.manager.model.web.SignUpRequest;
import com.github.changhee_choi.jubo.manager.service.AccountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Changhee Choi
 * @since 01/07/2020
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;
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
    void authorize() throws Exception {

        SignUpRequest signUpRequest = SignUpRequest.builder()
                .name("TestUser").email("test@email.com").password("password1")
                .churchName("My Church").churchMemberNum(10).build();

        accountService.signUp(signUpRequest);

        AuthenticationRequest request = new AuthenticationRequest("test@email.com", "password1");

        MvcResult result = mvc.perform(post("/authorize")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        assertThat(result.getResponse().getCookie("ACCESS_TOKEN")).isNotNull();
    }

    @Test
    void 가입되지_않은_이메일로_로그인을_요청하는_경우_UNAUTHORIZED_상태로_응답된다() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("test@email.com", "password1");

        mvc.perform(post("/authorize")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andReturn();
    }

    @Test
    void 틀린_비밀번호로_로그인을_요청하는_경우_UNAUTHORIZED_상태로_응답된다() throws Exception {
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .name("TestUser").email("test@email.com").password("password1")
                .churchName("My Church").churchMemberNum(10).build();
        accountService.signUp(signUpRequest);

        AuthenticationRequest request = new AuthenticationRequest("test@email.com", "password12");

        mvc.perform(post("/authorize")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andReturn();
    }
}