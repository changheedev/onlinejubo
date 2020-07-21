package com.github.changhee_choi.jubo.manager.service;

import com.github.changhee_choi.jubo.core.domain.church.Church;
import com.github.changhee_choi.jubo.core.domain.church.ChurchRepository;
import com.github.changhee_choi.jubo.core.domain.jubo.JuboDetails;
import com.github.changhee_choi.jubo.manager.web.payload.JuboRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
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

    @Test
    void 주보등록() {
        //given
        Church church = createChurch("MyChurch", 20);

        JuboRequest formPayload = JuboRequest.builder()
                .title("2020년 7월 12일 주보")
                .startDate(LocalDateTime.of(2020, 7, 12, 0, 0))
                .build();

        //when
        JuboDetails juboDetails = juboService.register(church.getId(), formPayload);

        //then
        assertThat(juboDetails.getId()).isNotNull();
        assertThat(juboDetails.getStartDate()).isEqualTo(formPayload.getStartDate());
        assertThat(juboDetails.getEndDate()).isEqualTo(formPayload.getStartDate().plusDays(6));
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

    private Church createChurch(String name, int memberNum) {
        Church church = Church.builder().name(name).memberNum(memberNum).build();
        return churchRepository.save(church);
    }
}