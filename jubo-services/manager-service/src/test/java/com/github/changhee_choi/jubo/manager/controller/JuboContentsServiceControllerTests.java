package com.github.changhee_choi.jubo.manager.controller;

import com.github.changhee_choi.jubo.manager.WebMvcTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Changhee Choi
 * @since 06/07/2020
 */
@SpringBootTest
class JuboContentsServiceControllerTests extends WebMvcTestSupport {

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
    }

    @Test
    void 주보_컨텐츠_등록에_성공한다() throws Exception {
        //given
        String contentPayload = createJuboContentPayload("주일 1부예배", "내용", attachmentIds);

        //when, then
        mockMvc.perform(post("/jubo/{id}/contents", juboId)
                .cookie(createTokenCookie(churchId, managerRole))
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value("주일 1부예배"))
                .andExpect(jsonPath("$.content").value("내용"))
                .andExpect(jsonPath("$.attachments[0].id").isNotEmpty())
                .andExpect(jsonPath("$.attachments[0].path").value("/test/upload/path/testUUID1.jpg"))
                .andExpect(jsonPath("$.attachments[0].originName").value("testFile1.jpg"))
                .andExpect(jsonPath("$.attachments[0].fileType").value("IMAGE"));
    }

    @Test
    void 주보_컨텐츠의_제목이_입력되지_않은_경우_BadRequest_상태가_응답된다() throws Exception {
        //given
        String contentPayload = createJuboContentPayload("", "내용", new ArrayList<>());

        //when, then
        mockMvc.perform(post("/jubo/{id}/contents", juboId)
                .cookie(createTokenCookie(churchId, managerRole))
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentPayload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 주보_컨텐츠의_내용이_입력되지_않은_경우_BadRequest_상태가_응답된다() throws Exception {
        //given
        String contentPayload = createJuboContentPayload("주일 1부 예배", "", new ArrayList<>());

        //when, then
        mockMvc.perform(post("/jubo/{id}/contents", juboId)
                .cookie(createTokenCookie(churchId, managerRole))
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentPayload))
                .andExpect(status().isBadRequest());
    }

    private String createJuboContentPayload(String title, String content, List<UUID> attachmentIds) {
        StringBuilder payLoadBuilder = new StringBuilder();

        payLoadBuilder.append("{")
                .append("\"title\" : \"").append(title).append("\",")
                .append("\"content\" : \"").append(content).append("\",")
                .append("\"attachmentIds\" : [");

        for (int i = 0; i < attachmentIds.size(); i++) {
            if (i != 0) {
                payLoadBuilder.append(", ");
            }
            payLoadBuilder.append("\"").append(attachmentIds.get(i).toString()).append("\"");
        }
        payLoadBuilder.append("]}");
        return payLoadBuilder.toString();
    }
}