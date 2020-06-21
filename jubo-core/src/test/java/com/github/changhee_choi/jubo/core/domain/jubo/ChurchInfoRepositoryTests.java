package com.github.changhee_choi.jubo.core.domain.jubo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Changhee Choi
 * @since 21/06/2020
 */
@DataJpaTest
class ChurchInfoRepositoryTests {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ChurchInfoRepository churchInfoRepository;

    private ChurchInfo createChurchInfoEntity() {
        ChurchInfo churchInfo = new ChurchInfo("test_church", 30);
        return churchInfoRepository.save(churchInfo);
    }

    @Test
    void create() {
        ChurchInfo churchInfo = createChurchInfoEntity();

        assertThat(churchInfo.getId()).isNotNull();
        assertThat(churchInfo.getId().toString().length()).isEqualTo(36);
        logger.info(churchInfo.getId().toString());

        assertThat(churchInfo.getName()).isEqualTo("test_church");
        assertThat(churchInfo.getMemberNum()).isEqualTo(30);
        assertThat(churchInfo.getCreatedBy()).isEqualTo("testUser(test@email.com)");
        assertThat(churchInfo.getLastModifiedBy()).isEqualTo("testUser(test@email.com)");
        assertThat(churchInfo.getCreatedDate()).isNotNull();
        assertThat(churchInfo.getLastModifiedDate()).isNotNull();
    }

    @Test
    void read() {
        ChurchInfo churchInfo = createChurchInfoEntity();

        Optional<ChurchInfo> optionalChurchInfo = churchInfoRepository.findById(churchInfo.getId());
        assertThat(optionalChurchInfo.isPresent()).isTrue();
    }

    @Test
    void update() {
        ChurchInfo churchInfo = createChurchInfoEntity();

        churchInfo.updateName("updated_church");
        churchInfo.updateMemberNum(50);
        ChurchInfo updatedChurchInfo = churchInfoRepository.save(churchInfo);
        churchInfoRepository.flush();

        assertThat(updatedChurchInfo.getName()).isEqualTo("updated_church");
        assertThat(updatedChurchInfo.getMemberNum()).isEqualTo(50);
        assertThat(updatedChurchInfo.getLastModifiedDate()).isNotEqualTo(updatedChurchInfo.getCreatedDate());
    }

    @Test
    void delete() {
        ChurchInfo churchInfo = createChurchInfoEntity();
        churchInfoRepository.delete(churchInfo);

        Optional<ChurchInfo> optionalChurchInfo = churchInfoRepository.findById(churchInfo.getId());
        assertThat(optionalChurchInfo.isPresent()).isFalse();
    }
}