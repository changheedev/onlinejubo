package com.github.changhee_choi.jubo.manager.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Changhee Choi
 * @since 06/07/2020
 */
@AutoConfigureMockMvc
@SpringBootTest
class JuboServiceControllerTests {
    @Autowired
    private MockMvc mockMvc;

    private String generateTestJuboContentPayLoads() {
        String payLoad = "[" +
                "{" +
                "\"title\" : \"주일 1부 예배\"," +
                "\"content\" : \"<p>주일 1부 예배순서 내용입니다.</p>\"" +
                "}," +
                "{" +
                "\"title\" : \"주일 2부 예배\"," +
                "\"content\" : \"<p>주일 2부 예배순서 내용입니다.</p>\"" +
                "}," +
                "{" +
                "\"title\" : \"교회소식\"," +
                "\"content\" : \"<p>교회소식 내용입니다.</p>\"," +
                "\"attachmentIds\" : [\"" + UUID.randomUUID().toString() + "\",\"" + UUID.randomUUID().toString() + "\"]" +
                "}" +
                "]";
        return payLoad;
    }

    @Test
    @WithMockUser(username = "TestUser", roles = {"CHURCH_MANAGER"})
    void 주보등록() throws Exception {
        String payLoad = "{" +
                "\"title\" : \"2020년 7월 6일 주보\"," +
                "\"churchId\" : \"" + UUID.randomUUID().toString() + "\"," +
                "\"startDate\" : \"2020-07-12 00:00:00\"," +
                "\"juboContents\" : " + generateTestJuboContentPayLoads()
                + "}";

        mockMvc.perform(post("/jubo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payLoad))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "TestUser", roles = {"USER"})
    void 주보등록_요청을_보냈을때_유저가_Church_Manager_Role을_가지지_않은_경우_Forbidden_상태가_응답된다() throws Exception {
        String payLoad = "{" +
                "\"title\" : \"2020년 7월 6일 주보\"," +
                "\"churchId\" : \"" + UUID.randomUUID().toString() + "\"," +
                "\"startDate\" : \"2020-07-12 00:00:00\"," +
                "\"juboContents\" : " + generateTestJuboContentPayLoads()
                + "}";

        mockMvc.perform(post("/jubo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payLoad))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "TestUser", roles = {"CHURCH_MANAGER"})
    void 주보등록_요청을_보냈을때_제목이_입력되지_않은경우_BadRequest_상태가_응답된다() throws Exception {
        String payLoad = "{" +
                "\"title\" : \"\"," +
                "\"churchId\" : \"" + UUID.randomUUID().toString() + "\"," +
                "\"startDate\" : \"2020-07-12 00:00:00\"," +
                "\"juboContents\" : " + generateTestJuboContentPayLoads()
                + "}";

        mockMvc.perform(post("/jubo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payLoad))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "TestUser", roles = {"CHURCH_MANAGER"})
    void 주보등록_요청을_보냈을때_교회Id가_입력되지_않은경우_BadRequest_상태가_응답된다() throws Exception {
        String payLoad = "{" +
                "\"title\" : \"2020년 7월 6일 주보\"," +
                "\"churchId\" : \"\"," +
                "\"startDate\" : \"2020-07-12 00:00:00\"," +
                "\"juboContents\" : " + generateTestJuboContentPayLoads()
                + "}";

        mockMvc.perform(post("/jubo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payLoad))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "TestUser", roles = {"CHURCH_MANAGER"})
    void 주보등록_요청을_보냈을때_교회Id가_UUID타입이_아닌경우_BadRequest_상태가_응답된다() throws Exception {
        String payLoad = "{" +
                "\"title\" : \"2020년 7월 6일 주보\"," +
                "\"churchId\" : \"abcdefg123\"," +
                "\"startDate\" : \"2020-07-12 00:00:00\"," +
                "\"juboContents\" : " + generateTestJuboContentPayLoads()
                + "}";

        mockMvc.perform(post("/jubo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payLoad))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "TestUser", roles = {"CHURCH_MANAGER"})
    void 주보등록_요청을_보냈을때_시작날짜가_입력되지_않은경우_BadRequest_상태가_응답된다() throws Exception {
        String payLoad = "{" +
                "\"title\" : \"2020년 7월 6일 주보\"," +
                "\"churchId\" : \"" + UUID.randomUUID().toString() + "\"," +
                "\"startDate\" : \"\"," +
                "\"juboContents\" : " + generateTestJuboContentPayLoads()
                + "}";

        mockMvc.perform(post("/jubo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payLoad))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "TestUser", roles = {"CHURCH_MANAGER"})
    void 주보등록_요청을_보냈을때_시작날짜의_형식이_yyyy_MM_dd__HH_mm_ss가_아닌경우_BadRequest_상태가_응답된다() throws Exception {
        String payLoad = "{" +
                "\"title\" : \"2020년 7월 6일 주보\"," +
                "\"churchId\" : \"" + UUID.randomUUID().toString() + "\"," +
                "\"startDate\" : \"2020-07-12\"," +
                "\"juboContents\" : " + generateTestJuboContentPayLoads()
                + "}";

        mockMvc.perform(post("/jubo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payLoad))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "TestUser", roles = {"CHURCH_MANAGER"})
    void 주보등록_요청을_보냈을때_juboContents의_사이즈가_0인_경우_BadRequest_상태가_응답된다() throws Exception {
        String payLoad = "{" +
                "\"title\" : \"2020년 7월 6일 주보\"," +
                "\"churchId\" : \"" + UUID.randomUUID().toString() + "\"," +
                "\"startDate\" : \"2020-07-12 00:00:00\"," +
                "\"juboContents\" : []"
                + "}";

        mockMvc.perform(post("/jubo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payLoad))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "TestUser", roles = {"CHURCH_MANAGER"})
    void 주보등록_요청을_보냈을때_juboContents의_제목이_입력되지_않은_경우_BadRequest_상태가_응답된다() throws Exception {
        String payLoad = "{" +
                "\"title\" : \"2020년 7월 6일 주보\"," +
                "\"churchId\" : \"" + UUID.randomUUID().toString() + "\"," +
                "\"startDate\" : \"2020-07-12 00:00:00\"," +
                "\"juboContents\" : " +
                "[{" +
                "\"title\" : \"\"," +
                "\"content\" : \"<p>주일 1부 예배순서 내용입니다.</p>\"" +
                "}]"
                + "}";

        mockMvc.perform(post("/jubo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payLoad))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "TestUser", roles = {"CHURCH_MANAGER"})
    void 주보등록_요청을_보냈을때_juboContents의_내용이_입력되지_않은_경우_BadRequest_상태가_응답된다() throws Exception {
        String payLoad = "{" +
                "\"title\" : \"2020년 7월 6일 주보\"," +
                "\"churchId\" : \"" + UUID.randomUUID().toString() + "\"," +
                "\"startDate\" : \"2020-07-12 00:00:00\"," +
                "\"juboContents\" : " +
                "[{" +
                "\"title\" : \"주일 1부 예배\"," +
                "\"content\" : \"\"" +
                "}]"
                + "}";

        mockMvc.perform(post("/jubo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payLoad))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}