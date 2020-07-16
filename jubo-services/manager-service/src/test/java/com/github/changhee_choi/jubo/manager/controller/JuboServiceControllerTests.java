package com.github.changhee_choi.jubo.manager.controller;

import com.github.changhee_choi.jubo.core.domain.attachment.Attachment;
import com.github.changhee_choi.jubo.core.domain.attachment.AttachmentRepository;
import com.github.changhee_choi.jubo.core.domain.attachment.FileType;
import com.github.changhee_choi.jubo.core.domain.church.Church;
import com.github.changhee_choi.jubo.core.domain.church.ChurchRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
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
    @Autowired
    private ChurchRepository churchRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;

    private Church createChurch(String name, int memberNum) {
        Church church = Church.builder().name(name).memberNum(memberNum).build();
        return churchRepository.save(church);
    }

    private Attachment createAttachment(String filename, FileType fileType) {
        Attachment attachment = Attachment.builder().path("/test/upload/path/" + UUID.randomUUID() + ".jpg")
                .originName(filename).fileType(FileType.IMAGE).build();
        return attachmentRepository.save(attachment);
    }

    private String createJuboContentPayLoad(String title, String content, List<UUID> attachmentIds) {
        StringBuilder payLoadBuilder = new StringBuilder();

        payLoadBuilder.append("{")
                .append("\"title\" : \"").append(title).append("\",")
                .append("\"content\" : \"").append(content).append("\"")
                .append("\"attachmentIds\" : [\"");

        for (int i = 0; i < attachmentIds.size(); i++) {
            payLoadBuilder.append(attachmentIds.get(i).toString());
            if (i != 0 || i < attachmentIds.size() - 1) {
                payLoadBuilder.append(", ");
            }
        }
        payLoadBuilder.append("\"]}");
        return payLoadBuilder.toString();
    }

    @Test
    @WithMockUser(username = "TestUser", roles = {"CHURCH_MANAGER"})
    void 주보등록() throws Exception {

        Church church = createChurch("MyChurch", 20);
        Attachment attachment = createAttachment("testFile1.jpg", FileType.IMAGE);

        StringBuilder payLoadBuilder = new StringBuilder();
        payLoadBuilder.append("{")
                .append("\"title\" : \"2020년 7월 6일 주보\",")
                .append("\"churchId\" : \"").append(church.getId()).append("\",")
                .append("\"startDate\" : \"2020-07-12 00:00:00\",")
                .append("\"juboContents\" : [")
                .append(createJuboContentPayLoad("주일 1부예배", "내용", Arrays.asList(attachment.getId())))
                .append("]}");

        mockMvc.perform(post("/jubo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payLoadBuilder.toString()))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "TestUser", roles = {"USER"})
    void 주보등록_요청을_보냈을때_유저가_Church_Manager_Role을_가지지_않은_경우_Forbidden_상태가_응답된다() throws Exception {
        mockMvc.perform(post("/jubo")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "TestUser", roles = {"CHURCH_MANAGER"})
    void 주보등록_요청을_보냈을때_제목이_입력되지_않은경우_BadRequest_상태가_응답된다() throws Exception {
        StringBuilder payLoadBuilder = new StringBuilder();
        payLoadBuilder.append("{")
                .append("\"title\" : \"\",")
                .append("\"churchId\" : \"").append(UUID.randomUUID()).append("\",")
                .append("\"startDate\" : \"2020-07-12 00:00:00\",")
                .append("\"juboContents\" : [")
                .append(createJuboContentPayLoad("주일 1부예배", "내용", Arrays.asList(UUID.randomUUID())))
                .append("]}");

        mockMvc.perform(post("/jubo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payLoadBuilder.toString()))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "TestUser", roles = {"CHURCH_MANAGER"})
    void 주보등록_요청을_보냈을때_교회Id가_입력되지_않은경우_BadRequest_상태가_응답된다() throws Exception {
        StringBuilder payLoadBuilder = new StringBuilder();
        payLoadBuilder.append("{")
                .append("\"title\" : \"2020년 7월 6일 주보\",")
                .append("\"churchId\" : \"\",")
                .append("\"startDate\" : \"2020-07-12 00:00:00\",")
                .append("\"juboContents\" : [")
                .append(createJuboContentPayLoad("주일 1부예배", "내용", Arrays.asList(UUID.randomUUID())))
                .append("]}");

        mockMvc.perform(post("/jubo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payLoadBuilder.toString()))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "TestUser", roles = {"CHURCH_MANAGER"})
    void 주보등록_요청을_보냈을때_교회Id가_UUID타입이_아닌경우_BadRequest_상태가_응답된다() throws Exception {
        StringBuilder payLoadBuilder = new StringBuilder();
        payLoadBuilder.append("{")
                .append("\"title\" : \"2020년 7월 6일 주보\",")
                .append("\"churchId\" : \"").append("NotUUIDTypeString").append("\",")
                .append("\"startDate\" : \"2020-07-12 00:00:00\",")
                .append("\"juboContents\" : [")
                .append(createJuboContentPayLoad("주일 1부예배", "내용", Arrays.asList(UUID.randomUUID())))
                .append("]}");

        mockMvc.perform(post("/jubo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payLoadBuilder.toString()))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "TestUser", roles = {"CHURCH_MANAGER"})
    void 주보등록_요청을_보냈을때_시작날짜가_입력되지_않은경우_BadRequest_상태가_응답된다() throws Exception {
        StringBuilder payLoadBuilder = new StringBuilder();
        payLoadBuilder.append("{")
                .append("\"title\" : \"2020년 7월 6일 주보\",")
                .append("\"churchId\" : \"").append(UUID.randomUUID()).append("\",")
                .append("\"startDate\" : \"\",")
                .append("\"juboContents\" : [")
                .append(createJuboContentPayLoad("주일 1부예배", "내용", Arrays.asList(UUID.randomUUID())))
                .append("]}");

        mockMvc.perform(post("/jubo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payLoadBuilder.toString()))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "TestUser", roles = {"CHURCH_MANAGER"})
    void 주보등록_요청을_보냈을때_시작날짜의_형식이_yyyy_MM_dd__HH_mm_ss가_아닌경우_BadRequest_상태가_응답된다() throws Exception {
        StringBuilder payLoadBuilder = new StringBuilder();
        payLoadBuilder.append("{")
                .append("\"title\" : \"2020년 7월 6일 주보\",")
                .append("\"churchId\" : \"").append(UUID.randomUUID()).append("\",")
                .append("\"startDate\" : \"2020-07-12\",")
                .append("\"juboContents\" : [")
                .append(createJuboContentPayLoad("주일 1부예배", "내용", Arrays.asList(UUID.randomUUID())))
                .append("]}");

        mockMvc.perform(post("/jubo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payLoadBuilder.toString()))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "TestUser", roles = {"CHURCH_MANAGER"})
    void 주보등록_요청을_보냈을때_juboContents의_사이즈가_0인_경우_BadRequest_상태가_응답된다() throws Exception {
        StringBuilder payLoadBuilder = new StringBuilder();
        payLoadBuilder.append("{")
                .append("\"title\" : \"2020년 7월 6일 주보\",")
                .append("\"churchId\" : \"").append(UUID.randomUUID()).append("\",")
                .append("\"startDate\" : \"2020-07-12 00:00:00\",")
                .append("\"juboContents\" : []")
                .append("}");

        mockMvc.perform(post("/jubo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payLoadBuilder.toString()))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "TestUser", roles = {"CHURCH_MANAGER"})
    void 주보등록_요청을_보냈을때_juboContents의_제목이_입력되지_않은_경우_BadRequest_상태가_응답된다() throws Exception {
        StringBuilder payLoadBuilder = new StringBuilder();
        payLoadBuilder.append("{")
                .append("\"title\" : \"2020년 7월 6일 주보\",")
                .append("\"churchId\" : \"").append(UUID.randomUUID()).append("\",")
                .append("\"startDate\" : \"2020-07-12 00:00:00\",")
                .append("\"juboContents\" : [")
                .append(createJuboContentPayLoad("", "내용", Arrays.asList(UUID.randomUUID())))
                .append("]}");

        mockMvc.perform(post("/jubo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payLoadBuilder.toString()))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "TestUser", roles = {"CHURCH_MANAGER"})
    void 주보등록_요청을_보냈을때_juboContents의_내용이_입력되지_않은_경우_BadRequest_상태가_응답된다() throws Exception {
        StringBuilder payLoadBuilder = new StringBuilder();
        payLoadBuilder.append("{")
                .append("\"title\" : \"2020년 7월 6일 주보\",")
                .append("\"churchId\" : \"").append(UUID.randomUUID()).append("\",")
                .append("\"startDate\" : \"2020-07-12 00:00:00\",")
                .append("\"juboContents\" : [")
                .append(createJuboContentPayLoad("주일 1부예배", "", Arrays.asList(UUID.randomUUID())))
                .append("]}");

        mockMvc.perform(post("/jubo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payLoadBuilder.toString()))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}