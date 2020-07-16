package com.github.changhee_choi.jubo.manager.service;

import com.github.changhee_choi.jubo.core.domain.attachment.Attachment;
import com.github.changhee_choi.jubo.core.domain.attachment.AttachmentNotFoundException;
import com.github.changhee_choi.jubo.core.domain.attachment.AttachmentRepository;
import com.github.changhee_choi.jubo.core.domain.attachment.FileType;
import com.github.changhee_choi.jubo.core.domain.church.Church;
import com.github.changhee_choi.jubo.core.domain.church.ChurchNotFoundException;
import com.github.changhee_choi.jubo.core.domain.church.ChurchRepository;
import com.github.changhee_choi.jubo.core.domain.jubo.JuboDetails;
import com.github.changhee_choi.jubo.manager.web.payload.JuboContentPayload;
import com.github.changhee_choi.jubo.manager.web.payload.JuboRegistrationPayload;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Changhee Choi
 * @since 07/07/2020
 */
@SpringBootTest
class JuboServiceTests {

    @Autowired
    private JuboService juboService;
    @Autowired
    private ChurchRepository churchRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;

    @Test
    void 주보등록() {
        //given
        Church church = createChurch("MyChurch", 20);
        Attachment attachment1 = createAttachment("testFile1.jpg", FileType.IMAGE);
        Attachment attachment2 = createAttachment("testFile2.jpg", FileType.IMAGE);

        JuboRegistrationPayload formPayload = JuboRegistrationPayload.builder()
                .churchId(church.getId())
                .title("2020년 7월 12일 주보")
                .startDate(LocalDateTime.of(2020, 7, 12, 0, 0))
                .juboContents(Arrays.asList(
                        createJuboContentPayload("주일 1부 예배", "내용", new ArrayList<>()),
                        createJuboContentPayload(
                                "주일 2부 예배", "내용", Arrays.asList(attachment1.getId(), attachment2.getId())
                        )
                ))
                .build();

        //when
        JuboDetails juboDetails = juboService.register(formPayload);

        //then
        assertThat(juboDetails.getId()).isNotNull();
        assertThat(juboDetails.getStartDate()).isEqualTo(formPayload.getStartDate());
        assertThat(juboDetails.getEndDate()).isEqualTo(formPayload.getStartDate().plusDays(6));
        assertThat(juboDetails.getContents().size()).isEqualTo(formPayload.getJuboContents().size());
        for (int i = 0; i < juboDetails.getContents().size(); i++) {
            assertThat(juboDetails.getContents().get(i).getAttachments().size())
                    .isEqualTo(formPayload.getJuboContents().get(i).getAttachmentIds().size());
        }
    }

    @Test
    void 주보를_등록할때_등록되지_않은_교회Id가_사용된_경우_EntityNotFoundException이_던져진다() {
        JuboRegistrationPayload formPayload = JuboRegistrationPayload.builder()
                .churchId(UUID.randomUUID())
                .title("2020년 7월 12일 주보")
                .startDate(LocalDateTime.of(2020, 7, 12, 0, 0))
                .juboContents(Arrays.asList(createJuboContentPayload("주일 1부 예배", "내용", new ArrayList<>())))
                .build();

        assertThatThrownBy(() -> juboService.register(formPayload))
                .isInstanceOf(ChurchNotFoundException.class);
    }

    @Test
    void 주보를_등록할때_Payload의_첨부파일_Id_갯수와_실제_로드된_첨부파일_Entitiy_갯수가_다른경우_EntityNotFoundException이_던져진다() {
        Church church = createChurch("MyChurch", 20);
        Attachment attachment = createAttachment("testFile1.jpg", FileType.IMAGE);

        JuboRegistrationPayload formPayload = JuboRegistrationPayload.builder()
                .churchId(church.getId())
                .title("2020년 7월 12일 주보")
                .startDate(LocalDateTime.of(2020, 7, 12, 0, 0))
                .juboContents(Arrays.asList(
                        createJuboContentPayload(
                                "주일 1부 예배", "내용", Arrays.asList(attachment.getId(), UUID.randomUUID()))
                        )
                )
                .build();

        assertThatThrownBy(() -> juboService.register(formPayload))
                .isInstanceOf(AttachmentNotFoundException.class);
    }

    private Church createChurch(String name, int memberNum) {
        Church church = Church.builder().name(name).memberNum(memberNum).build();
        return churchRepository.save(church);
    }

    private Attachment createAttachment(String filename, FileType fileType) {
        Attachment attachment = Attachment.builder().path("/test/upload/path/" + UUID.randomUUID() + ".jpg")
                .originName(filename).fileType(FileType.IMAGE).build();
        return attachmentRepository.save(attachment);
    }

    private JuboContentPayload createJuboContentPayload(String title, String content, List<UUID> attachments) {
        JuboContentPayload contentPayload = JuboContentPayload.builder()
                .title(title)
                .content(content)
                .attachmentIds(attachments)
                .build();
        return contentPayload;
    }
}