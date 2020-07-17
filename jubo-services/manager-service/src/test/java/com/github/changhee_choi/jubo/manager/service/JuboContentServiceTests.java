package com.github.changhee_choi.jubo.manager.service;

import com.github.changhee_choi.jubo.core.domain.attachment.Attachment;
import com.github.changhee_choi.jubo.core.domain.attachment.AttachmentRepository;
import com.github.changhee_choi.jubo.core.domain.attachment.FileType;
import com.github.changhee_choi.jubo.core.domain.church.Church;
import com.github.changhee_choi.jubo.core.domain.church.ChurchRepository;
import com.github.changhee_choi.jubo.core.domain.jubo.Jubo;
import com.github.changhee_choi.jubo.core.domain.jubo.JuboContentDetails;
import com.github.changhee_choi.jubo.core.domain.jubo.JuboRepository;
import com.github.changhee_choi.jubo.manager.web.payload.JuboContentRegistrationPayload;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Changhee Choi
 * @since 17/07/2020
 */
@SpringBootTest
class JuboContentServiceTests {

    @Autowired
    private JuboContentService juboContentService;
    @Autowired
    private JuboRepository juboRepository;
    @Autowired
    private ChurchRepository churchRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;

    @Test
    void 주보_컨텐츠_등록() {
        //given
        Jubo jubo = createJubo("2020년 7월 17일 주보");
        Attachment attachment1 = createAttachment("testFile1.jpg", FileType.IMAGE);
        Attachment attachment2 = createAttachment("testFile2.jpg", FileType.IMAGE);

        JuboContentRegistrationPayload payload = JuboContentRegistrationPayload.builder()
                .title("주일 1부 예배")
                .content("내용")
                .attachmentIds(Arrays.asList(attachment1.getId(), attachment2.getId()))
                .build();

        //when
        JuboContentDetails contentDetails = juboContentService.register(jubo.getChurch().getId(), jubo.getId(), payload);

        //then
        assertThat(contentDetails.getId()).isNotNull();
        assertThat(contentDetails.getTitle()).isEqualTo(payload.getTitle());
        assertThat(contentDetails.getContent()).isEqualTo(payload.getContent());
        assertThat(contentDetails.getAttachments().size()).isEqualTo(payload.getAttachmentIds().size());
    }

    @Test
    void 주보를_등록할때_Payload의_첨부파일_Id_갯수와_실제_로드된_첨부파일_Entitiy_갯수가_다른경우_EntityNotFoundException이_던져진다() {
        //given
        Jubo jubo = createJubo("2020년 7월 17일 주보");
        Attachment attachment = createAttachment("testFile1.jpg", FileType.IMAGE);

        JuboContentRegistrationPayload payload = JuboContentRegistrationPayload.builder()
                .title("주일 1부 예배")
                .content("내용")
                .attachmentIds(Arrays.asList(attachment.getId(), UUID.randomUUID()))
                .build();

        //when, then
        assertThatThrownBy(() -> juboContentService.register(jubo.getChurch().getId(), jubo.getId(), payload))
                .isInstanceOf(EntityNotFoundException.class);

    }

    @Test
    void 주보의_ChurchId와_파라미터로_넘겨진_매니저의_ChurchId가_다르면_AccessDeniedException이_던져진다() {
        //given
        Jubo jubo = createJubo("2020년 7월 17일 주보");

        JuboContentRegistrationPayload payload = JuboContentRegistrationPayload.builder()
                .title("주일 1부 예배")
                .content("내용")
                .attachmentIds(new ArrayList<>())
                .build();

        //when, then
        assertThatThrownBy(() -> juboContentService.register(UUID.randomUUID(), jubo.getId(), payload))
                .isInstanceOf(AccessDeniedException.class);
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
}