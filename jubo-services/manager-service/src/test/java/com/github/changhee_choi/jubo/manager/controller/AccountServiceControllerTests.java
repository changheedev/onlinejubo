package com.github.changhee_choi.jubo.manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.changhee_choi.jubo.core.domain.user.User;
import com.github.changhee_choi.jubo.core.domain.user.UserRepository;
import com.github.changhee_choi.jubo.manager.model.web.SignUpRequest;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Changhee Choi
 * @since 24/06/2020
 */
@AutoConfigureMockMvc
@SpringBootTest
class AccountServiceControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    void signUp() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .name("testUser")
                .email("test@email.com")
                .password("pwd1!@#$%^&*()-_+=")
                .churchName("My Church")
                .churchMemberNum(20)
                .build();

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 중복된_이메일로_회원가입이_요청된_경우_UnprocessableEntity_상태로_응답된다() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .name("testUser")
                .email("test@email.com")
                .password("pwd1!@#$%^&*()-_+=")
                .churchName("My Church")
                .churchMemberNum(20)
                .build();

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());

        //중복된 이메일 가입 시도
        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void 회원가입시_패스워드에_알파벳만_사용된_경우_BadRequest_상태로_응답된다() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .name("testUser")
                .email("test@email.com")
                .password("password")
                .churchName("My Church")
                .churchMemberNum(20)
                .build();

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 회원가입시_패스워드에_숫자만_사용된_경우_BadRequest_상태로_응답된다() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .name("testUser")
                .email("test@email.com")
                .password("1234567890")
                .churchName("My Church")
                .churchMemberNum(20)
                .build();

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 회원가입시_패스워드_길이가_MIN_SIZE_보다_짧은_경우_BadRequest_상태로_응답된다() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .name("testUser")
                .email("test@email.com")
                .password("pwd1234") //min size = 8
                .churchName("My Church")
                .churchMemberNum(20)
                .build();

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 회원가입시_패스워드_길이가_MAX_SIZE_보다_긴_경우_BadRequest_상태로_응답된다() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .name("testUser")
                .email("test@email.com")
                .password("pwd1234!@#$%^&*(){}[]") //max size = 20
                .churchName("My Church")
                .churchMemberNum(20)
                .build();

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 회원가입시_패스워드에_Null인_경우_BadRequest_상태로_응답된다() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .name("testUser")
                .email("test@email.com")
                .password(null)
                .churchName("My Church")
                .churchMemberNum(20)
                .build();

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 회원가입시_이메일_패턴이_올바르지_않은경우_BadRequest_상태로_응답된다() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .name("testUser")
                .email("test@emailcom")
                .password("password1")
                .churchName("My Church")
                .churchMemberNum(20)
                .build();

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 회원가입시_이메일이_Null인_경우_BadRequest_상태로_응답된다() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .name("testUser")
                .email(null)
                .password("password1")
                .churchName("My Church")
                .churchMemberNum(20)
                .build();

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 회원가입시_이름에_underscore를_제외한_특수문자가_사용된_경우_BadRequest_상태로_응답된다() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .name("testUser!")
                .email("test@email.com")
                .password("password1")
                .churchName("My Church")
                .churchMemberNum(20)
                .build();

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 회원가입시_이름이_MIN_SIZE_보다_짧은_경우_BadRequest_상태로_응답된다() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .name("t") //min size = 2
                .email("test@email.com")
                .password("password1")
                .churchName("My Church")
                .churchMemberNum(20)
                .build();

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 회원가입시_이름이_MAX_SIZE_보다_긴_경우_BadRequest_상태로_응답된다() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .name("abcdefghijklmnopqrstu") //max size = 20
                .email("test@email.com")
                .password("password1")
                .churchName("My Church")
                .churchMemberNum(20)
                .build();

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 회원가입시_이름이_Null인_경우_BadRequest_상태로_응답된다() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .name(null) //max size = 20
                .email("test@email.com")
                .password("password1")
                .churchName("My Church")
                .churchMemberNum(20)
                .build();

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 회원가입시_교회이름이_MIN_SIZE_보다_짧은_경우_BadRequest_상태로_응답된다() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .name("testUser")
                .email("test@email.com")
                .password("pwd1!@#$%^&*()-_+=")
                .churchName("a")
                .churchMemberNum(20)
                .build();

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 회원가입시_교회이름이_MAX_SIZE_보다_짧은_경우_BadRequest_상태로_응답된다() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .name("testUser")
                .email("test@email.com")
                .password("pwd1!@#$%^&*()-_+=")
                .churchName(RandomString.make(51)) //max size: 50
                .churchMemberNum(20)
                .build();

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 회원가입시_교회이름에_알파벳과_한글을_제외한_문자가_사용된_경우_BadRequest_상태로_응답된다() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .name("testUser")
                .email("test@email.com")
                .password("pwd1!@#$%^&*()-_+=")
                .churchName("My Church1")
                .churchMemberNum(20)
                .build();

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 회원가입시_교회이름이_Null인_경우_BadRequest_상태로_응답된다() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .name("testUser")
                .email("test@email.com")
                .password("pwd1!@#$%^&*()-_+=")
                .churchName(null)
                .churchMemberNum(20)
                .build();

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 회원가입시_교회인원수가_Null인_경우_BadRequest_상태로_응답된다() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .name("testUser")
                .email("test@email.com")
                .password("pwd1!@#$%^&*()-_+=")
                .churchName("My Church")
                .churchMemberNum(null)
                .build();

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}