package com.github.changhee_choi.jubo.manager.service;

import com.github.changhee_choi.jubo.core.domain.attachment.AttachmentRepository;
import com.github.changhee_choi.jubo.core.domain.jubo.Jubo;
import com.github.changhee_choi.jubo.core.domain.jubo.JuboDetails;
import com.github.changhee_choi.jubo.core.domain.jubo.JuboRepository;
import com.github.changhee_choi.jubo.manager.TestParameterSupport;
import com.github.changhee_choi.jubo.manager.web.payload.JuboContentRequest;
import com.github.changhee_choi.jubo.manager.web.payload.JuboRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
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
class JuboServiceTests extends TestParameterSupport {

    @Autowired
    private JuboService juboService;
    @Autowired
    private JuboRepository juboRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;

    @Test
    void 주보등록() {
        //given
        JuboContentRequest contentPayload1 = JuboContentRequest.builder()
                .title("주일 1부 예배")
                .content(timetableTypeContentSample)
                .build();

        JuboContentRequest contentPayload2 = JuboContentRequest.builder()
                .title("교회소식")
                .content(postTypeContentSample)
                .attachmentIds(attachmentIds)
                .build();

        List<JuboContentRequest> contents = Arrays.asList(contentPayload1, contentPayload2);

        JuboRequest formPayload = JuboRequest.builder()
                .title("2020년 7월 12일 주보")
                .startDate(LocalDateTime.of(2020, 7, 12, 0, 0))
                .contents(contents)
                .build();

        //when
        JuboDetails juboDetails = juboService.register(churchId, formPayload);

        //then
        assertThat(juboDetails).isNotNull();
    }

    @Test
    void 주보를_등록할때_등록되지_않은_교회Id가_사용된_경우_EntityNotFoundException이_던져진다() {
        JuboRequest formPayload = JuboRequest.builder()
                .title("2020년 7월 12일 주보")
                .startDate(LocalDateTime.of(2020, 7, 12, 0, 0))
                .build();

        assertThatThrownBy(() -> juboService.register(UUID.randomUUID(), formPayload))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void 주보를_업데이트_하면_컨텐츠_리스트가_새로_등록된다() {
        //given
        JuboContentRequest contentPayload1 = JuboContentRequest.builder()
                .title("주일 1부 예배")
                .content(timetableTypeContentSample)
                .build();

        JuboContentRequest contentPayload2 = JuboContentRequest.builder()
                .title("주일 2부 예배")
                .content(timetableTypeContentSample)
                .build();

        JuboContentRequest contentPayload3 = JuboContentRequest.builder()
                .title("교회소식")
                .content(postTypeContentSample)
                .attachmentIds(attachmentIds)
                .build();

        List<JuboContentRequest> contents = Arrays.asList(contentPayload1, contentPayload2, contentPayload3);

        JuboRequest payload = JuboRequest.builder()
                .title("2020년 7월 12일 주보")
                .startDate(LocalDateTime.of(2020, 7, 12, 0, 0))
                .contents(contents)
                .build();

        //when
        JuboDetails updatedJuboDetails = juboService.update(churchId, juboId, payload);

        //then
        assertThat(updatedJuboDetails.getContents().size()).isEqualTo(3);
    }

    @Test
    void 요청된_ID로_주보_데이터를_찾을_수_없는_경우_업데이트에_실패한다() {
        assertThatThrownBy(() -> {
            juboService.update(UUID.randomUUID(), 10L, JuboRequest.builder()
                    .title("주보 제목")
                    .startDate(LocalDateTime.of(2020, 07, 21, 0, 0))
                    .build());
        }).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void 유저의_ChurchId_와_주보의_ChurchId_가_다르면_업데이트에_실패한다() {
        assertThatThrownBy(() -> {
            juboService.update(UUID.randomUUID(), juboId, JuboRequest.builder()
                    .title("업데이트 된 제목")
                    .startDate(LocalDateTime.of(2020, 07, 21, 0, 0))
                    .build());
        }).isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void 주보_삭제에_성공한다() {
        //when
        juboService.delete(churchId, juboId);

        //then
        Jubo deletedJubo = juboRepository.findById(juboId).get();
        assertThat(deletedJubo.isDeleted()).isTrue();
    }

    @Test
    void 요청된_ID로_주보_데이터를_찾을_수_없는_경우_삭제에_실패한다() {
        assertThatThrownBy(() -> juboService.delete(churchId, 10L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void 유저의_ChurchId_와_주보의_ChurchId_가_다르면_삭제에_실패한다() {
        assertThatThrownBy(() -> juboService.delete(UUID.randomUUID(), juboId))
                .isInstanceOf(AccessDeniedException.class);
    }
}