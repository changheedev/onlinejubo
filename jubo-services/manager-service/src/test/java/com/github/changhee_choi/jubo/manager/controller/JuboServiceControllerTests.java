package com.github.changhee_choi.jubo.manager.controller;

import com.github.changhee_choi.jubo.manager.WebMvcTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Changhee Choi
 * @since 06/07/2020
 */
@SpringBootTest
class JuboServiceControllerTests extends WebMvcTestSupport {

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
    }

    @Test
    void 주보등록에_성공한다() throws Exception {
        String payload = createJuboPayload("2020년 7월 12일 주보", "2020-07-12 00:00:00");
        mockMvc.perform(post("/jubo")
                .cookie(createTokenCookie(churchId, managerRole))
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value("2020년 7월 12일 주보"))
                .andExpect(jsonPath("$.startDate").value("2020-07-12 00:00:00"))
                .andExpect(jsonPath("$.endDate").value("2020-07-18 00:00:00"))
                .andExpect(jsonPath("$.viewCount").value(0));
    }

    @Test
    void 주보등록_요청을_보냈을때_유저가_Church_Manager_Role을_가지지_않은_경우_Forbidden_상태가_응답된다() throws Exception {
        mockMvc.perform(post("/jubo")
                .cookie(createTokenCookie(churchId, "USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void 주보등록_요청을_보냈을때_제목이_입력되지_않은경우_BadRequest_상태가_응답된다() throws Exception {
        String payload = createJuboPayload("", "2020-07-12 00:00:00");

        mockMvc.perform(post("/jubo")
                .cookie(createTokenCookie(churchId, managerRole))
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 주보등록_요청을_보냈을때_시작날짜가_입력되지_않은경우_BadRequest_상태가_응답된다() throws Exception {
        String payload = createJuboPayload("2020년 7월 12일 주보", "");

        mockMvc.perform(post("/jubo")
                .cookie(createTokenCookie(churchId, managerRole))
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 주보등록_요청을_보냈을때_시작날짜의_형식이_yyyy_MM_dd__HH_mm_ss가_아닌경우_BadRequest_상태가_응답된다() throws Exception {
        String payload = createJuboPayload("2020년 7월 12일 주보", "2020-07-12");

        mockMvc.perform(post("/jubo")
                .cookie(createTokenCookie(churchId, managerRole))
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 주보_업데이트에_성공한다() throws Exception {
        String juboPayload = createJuboPayload("업데이트 된 제목", "2020-07-21 00:00:00");

        mockMvc.perform(put("/jubo/{id}", juboId)
                .cookie(createTokenCookie(churchId, managerRole))
                .content(juboPayload)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("업데이트 된 제목"))
                .andExpect(jsonPath("$.startDate").value("2020-07-21 00:00:00"))
                .andExpect(jsonPath("$.endDate").value("2020-07-27 00:00:00"));
    }

    @Test
    void 유저가_CHURCH_MANAGER_권한을_가지고_있지_않으면_업데이트에_실패한다() throws Exception {
        String juboPayload = createJuboPayload("업데이트 된 제목", "2020-07-21 00:00:00");

        mockMvc.perform(put("/jubo/{id}", juboId)
                .cookie(createTokenCookie(churchId, "USER"))
                .content(juboPayload)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void 유저의_ChurchId_와_주보의_ChurchId_가_다르면_업데이트에_실패한다() throws Exception {
        String juboPayload = createJuboPayload("업데이트 된 제목", "2020-07-21 00:00:00");

        mockMvc.perform(put("/jubo/{id}", juboId)
                .cookie(createTokenCookie(UUID.randomUUID(), managerRole))
                .content(juboPayload)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void 주보의_제목이_입력되지_않은_경우_업데이트에_실패한다() throws Exception {
        String juboPayload = createJuboPayload("", "2020-07-21 00:00:00");

        mockMvc.perform(put("/jubo/{id}", juboId)
                .cookie(createTokenCookie(churchId, managerRole))
                .content(juboPayload)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 주보의_시작_날짜가_입력되지_않은_경우_업데이트에_실패한다() throws Exception {
        String juboPayload = createJuboPayload("업데이트 된 제목", "");

        mockMvc.perform(put("/jubo/{id}", juboId)
                .cookie(createTokenCookie(churchId, managerRole))
                .content(juboPayload)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 주보의_시작_날짜의_형식이_LocalDateTime타입으로_매핑할_수_없는_경우_업데이트에_실패한다() throws Exception {
        String juboPayload = createJuboPayload("주보 제목", "2020-07-21");

        mockMvc.perform(put("/jubo/{id}", juboId)
                .cookie(createTokenCookie(churchId, managerRole))
                .content(juboPayload)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 주보_삭제에_성공한다() throws Exception {
        mockMvc.perform(delete("/jubo/{id}", juboId)
                .cookie(createTokenCookie(churchId, managerRole)))
                .andExpect(status().isOk());
    }

    @Test
    void 유저의_ChurchId_와_주보의_ChurchId_가_다르면_삭제에_실패한다() throws Exception {
        mockMvc.perform(delete("/jubo/{id}", juboId)
                .cookie(createTokenCookie(UUID.randomUUID(), managerRole)))
                .andExpect(status().isForbidden());
    }

    private String createJuboPayload(String title, String startDate) {
        StringBuilder payLoadBuilder = new StringBuilder();
        payLoadBuilder.append("{")
                .append("\"title\" : \"").append(title).append("\", ")
                .append("\"startDate\" : \"").append(startDate).append("\", ")
                .append("\"contents\" : [")
                .append("{")
                .append("\"title\" : \"주일 1부 예배\", ")
                .append("\"content\" : \"").append(timetableTypeContentSample).append("\", ")
                .append("\"attachmentIds\" : []")
                .append("}, ")
                .append("{")
                .append("\"title\" : \"교회 소식\", ")
                .append("\"content\" : \"").append(postTypeContentSample).append("\", ")
                .append("\"attachmentIds\" : [\"a0fd7051-c82e-11ea-a901-0242ac120003\"]")
                .append("}")
                .append("]")
                .append("}");
        return payLoadBuilder.toString();
    }
}