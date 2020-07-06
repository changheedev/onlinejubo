package com.github.changhee_choi.jubo.core.domain.jubo;

import com.github.changhee_choi.jubo.core.domain.church.Church;
import com.github.changhee_choi.jubo.core.domain.church.ChurchRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Changhee Choi
 * @since 21/06/2020
 */
@DataJpaTest
class ChurchRepositoryTests {
    @Autowired
    private ChurchRepository churchRepository;

    private Church createChurchEntity() {
        Church church = Church.builder().name("test_church").memberNum(30).build();
        return churchRepository.save(church);
    }

    @Test
    void create() {
        Church church = createChurchEntity();

        assertThat(church.getId()).isNotNull();
        assertThat(church.getId().toString().length()).isEqualTo(36);
        System.out.println(church.getId());

        assertThat(church.getName()).isEqualTo("test_church");
        assertThat(church.getMemberNum()).isEqualTo(30);
        assertThat(church.getCreatedBy()).isEqualTo(1L);
        assertThat(church.getLastModifiedBy()).isEqualTo(1L);
        assertThat(church.getCreatedDate()).isNotNull();
        assertThat(church.getLastModifiedDate()).isNotNull();
    }

    @Test
    void read() {
        Church church = createChurchEntity();

        Optional<Church> optionalChurchInfo = churchRepository.findById(church.getId());
        assertThat(optionalChurchInfo.isPresent()).isTrue();
    }

    @Test
    void update() {
        Church church = createChurchEntity();

        church.updateName("updated_church");
        church.updateMemberNum(50);
        Church updatedChurchInfo = churchRepository.save(church);
        churchRepository.flush();

        assertThat(updatedChurchInfo.getName()).isEqualTo("updated_church");
        assertThat(updatedChurchInfo.getMemberNum()).isEqualTo(50);
        assertThat(updatedChurchInfo.getLastModifiedDate()).isNotEqualTo(updatedChurchInfo.getCreatedDate());
    }

    @Test
    void delete() {
        Church church = createChurchEntity();
        churchRepository.delete(church);

        Optional<Church> optionalChurchInfo = churchRepository.findById(church.getId());
        assertThat(optionalChurchInfo.isPresent()).isFalse();
    }
}