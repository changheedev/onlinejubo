package com.github.changhee_choi.jubo.manager.controller;

import com.github.changhee_choi.jubo.core.domain.attachment.Attachment;
import com.github.changhee_choi.jubo.core.domain.attachment.AttachmentRepository;
import com.github.changhee_choi.jubo.core.domain.attachment.FileType;
import com.github.changhee_choi.jubo.core.domain.church.Church;
import com.github.changhee_choi.jubo.core.domain.church.ChurchRepository;
import com.github.changhee_choi.jubo.core.domain.jubo.Jubo;
import com.github.changhee_choi.jubo.core.domain.jubo.JuboRepository;
import com.github.changhee_choi.jubo.manager.web.payload.ChurchManagerTokenClaims;
import com.github.changhee_choi.jubo.manager.web.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Changhee Choi
 * @since 06/07/2020
 */
@SpringBootTest
class JuboServiceControllerTests {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Autowired
    private ChurchRepository churchRepository;
    @Autowired
    private JuboRepository juboRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
    }

    @Test
    void 주보등록() throws Exception {
        Church church = createChurch("MyChurch", 20);
        String payload = createJuboPayload("2020년 7월 12일 주보", "2020-07-12 00:00:00");
        mockMvc.perform(post("/jubo")
                .cookie(createTokenCookie(church.getId(), "CHURCH_MANAGER"))
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
                .cookie(createTokenCookie(UUID.randomUUID(), "USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void 주보등록_요청을_보냈을때_제목이_입력되지_않은경우_BadRequest_상태가_응답된다() throws Exception {
        String payload = createJuboPayload("", "2020-07-12 00:00:00");

        mockMvc.perform(post("/jubo")
                .cookie(createTokenCookie(UUID.randomUUID(), "CHURCH_MANAGER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 주보등록_요청을_보냈을때_시작날짜가_입력되지_않은경우_BadRequest_상태가_응답된다() throws Exception {
        String payload = createJuboPayload("2020년 7월 12일 주보", "");

        mockMvc.perform(post("/jubo")
                .cookie(createTokenCookie(UUID.randomUUID(), "CHURCH_MANAGER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 주보등록_요청을_보냈을때_시작날짜의_형식이_yyyy_MM_dd__HH_mm_ss가_아닌경우_BadRequest_상태가_응답된다() throws Exception {
        String payload = createJuboPayload("2020년 7월 12일 주보", "2020-07-12");

        mockMvc.perform(post("/jubo")
                .cookie(createTokenCookie(UUID.randomUUID(), "CHURCH_MANAGER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 주보_컨텐츠_등록() throws Exception {
        //given
        Jubo jubo = createJubo("2020년 7월 17일 주보");
        Attachment attachment = createAttachment("testFile1.jpg", FileType.IMAGE);
        String contentPayload = createJuboContentPayload("주일 1부예배", "내용", Arrays.asList(attachment.getId()));

        //when, then
        mockMvc.perform(post("/jubo/{id}/contents", jubo.getId())
                .cookie(new Cookie("ACCESS_TOKEN", createAccessToken(jubo.getChurch().getId(), "ROLE_CHURCH_MANAGER")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value("주일 1부예배"))
                .andExpect(jsonPath("$.content").value("내용"))
                .andExpect(jsonPath("$.attachments[0].id").isNotEmpty())
                .andExpect(jsonPath("$.attachments[0].path").isNotEmpty())
                .andExpect(jsonPath("$.attachments[0].originName").value("testFile1.jpg"))
                .andExpect(jsonPath("$.attachments[0].fileType").value("IMAGE"));
    }

    @Test
    void 주보_컨텐츠의_제목이_입력되지_않은_경우_BadRequest_상태가_응답된다() throws Exception {
        //given
        String contentPayload = createJuboContentPayload("", "내용", new ArrayList<>());

        //when, then
        mockMvc.perform(post("/jubo/{id}/contents", UUID.randomUUID())
                .cookie(createTokenCookie(UUID.randomUUID(), "CHURCH_MANAGER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentPayload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 주보_컨텐츠의_내용이_입력되지_않은_경우_BadRequest_상태가_응답된다() throws Exception {
        //given
        String contentPayload = createJuboContentPayload("주일 1부 예배", "", new ArrayList<>());

        //when, then
        mockMvc.perform(post("/jubo/{id}/contents", UUID.randomUUID())
                .cookie(createTokenCookie(UUID.randomUUID(), "CHURCH_MANAGER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentPayload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 주보_업데이트에_성공한다() throws Exception {
        Jubo jubo = createJubo("주보 제목");
        String juboPayload = createJuboPayload("업데이트 된 제목", "2020-07-21 00:00:00");

        mockMvc.perform(put("/jubo/{id}", jubo.getId())
                .cookie(createTokenCookie(jubo.getChurch().getId(), "CHURCH_MANAGER"))
                .content(juboPayload)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("업데이트 된 제목"))
                .andExpect(jsonPath("$.startDate").value("2020-07-21 00:00:00"))
                .andExpect(jsonPath("$.endDate").value("2020-07-27 00:00:00"));
    }

    @Test
    void 유저가_CHURCH_MANAGER_권한을_가지고_있지_않으면_업데이트에_실패한다() throws Exception {
        Jubo jubo = createJubo("주보 제목");
        String juboPayload = createJuboPayload("업데이트 된 제목", "2020-07-21 00:00:00");

        mockMvc.perform(put("/jubo/{id}", jubo.getId())
                .cookie(createTokenCookie(jubo.getChurch().getId(), "USER"))
                .content(juboPayload)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void 유저의_ChurchId_와_주보의_ChurchId_가_다르면_업데이트에_실패한다() throws Exception {
        Jubo jubo = createJubo("주보 제목");
        String juboPayload = createJuboPayload("업데이트 된 제목", "2020-07-21 00:00:00");

        mockMvc.perform(put("/jubo/{id}", jubo.getId())
                .cookie(createTokenCookie(UUID.randomUUID(), "CHURCH_MANAGER"))
                .content(juboPayload)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void 주보의_제목이_입력되지_않은_경우_업데이트에_실패한다() throws Exception {
        Jubo jubo = createJubo("주보 제목");
        String juboPayload = createJuboPayload("", "2020-07-21 00:00:00");

        mockMvc.perform(put("/jubo/{id}", jubo.getId())
                .cookie(createTokenCookie(jubo.getChurch().getId(), "CHURCH_MANAGER"))
                .content(juboPayload)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 주보의_시작_날짜가_입력되지_않은_경우_업데이트에_실패한다() throws Exception {
        Jubo jubo = createJubo("주보 제목");
        String juboPayload = createJuboPayload("주보 제목", "");

        mockMvc.perform(put("/jubo/{id}", jubo.getId())
                .cookie(createTokenCookie(jubo.getChurch().getId(), "CHURCH_MANAGER"))
                .content(juboPayload)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 주보의_시작_날짜의_형식이_LocalDateTime타입으로_매핑할_수_없는_경우_업데이트에_실패한다() throws Exception {
        Jubo jubo = createJubo("주보 제목");
        String juboPayload = createJuboPayload("주보 제목", "2020-07-21");

        mockMvc.perform(put("/jubo/{id}", jubo.getId())
                .cookie(createTokenCookie(jubo.getChurch().getId(), "CHURCH_MANAGER"))
                .content(juboPayload)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private Church createChurch(String name, int memberNum) {
        Church church = Church.builder().name(name).memberNum(memberNum).build();
        return churchRepository.save(church);
    }

    private Jubo createJubo(String title) {
        Jubo jubo = Jubo.builder()
                .title(title)
                .startDate(LocalDateTime.of(2020, 7, 17, 0, 0)).build();

        Church church = createChurch("MyChurch", 20);
        jubo.setChurch(church);
        return juboRepository.save(jubo);
    }

    private Attachment createAttachment(String filename, FileType fileType) {
        Attachment attachment = Attachment.builder().path("/test/upload/path/" + UUID.randomUUID() + ".jpg")
                .originName(filename).fileType(FileType.IMAGE).build();
        return attachmentRepository.save(attachment);
    }

    private String createJuboPayload(String title, String startDate) {
        StringBuilder payLoadBuilder = new StringBuilder();
        payLoadBuilder.append("{")
                .append("\"title\" : \"").append(title).append("\",")
                .append("\"startDate\" : \"").append(startDate).append("\"")
                .append("}");
        return payLoadBuilder.toString();
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

    private String createAccessToken(UUID churchId, String role) {
        ChurchManagerTokenClaims claims = ChurchManagerTokenClaims.builder()
                .id(1L)
                .name("TestUser")
                .roles(Arrays.asList(role))
                .churchId(churchId)
                .build();

        return jwtUtil.generateToken(claims);
    }

    private Cookie createTokenCookie(UUID churchId, String role) {
        return new Cookie("ACCESS_TOKEN", createAccessToken(churchId, "ROLE_" + role));
    }
}