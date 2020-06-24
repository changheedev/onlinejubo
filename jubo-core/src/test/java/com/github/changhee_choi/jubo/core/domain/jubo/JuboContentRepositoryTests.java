package com.github.changhee_choi.jubo.core.domain.jubo;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Changhee Choi
 * @since 22/06/2020
 */
@DataJpaTest
class JuboContentRepositoryTests {

    @Autowired
    private JuboContentRepository juboContentRepository;

    private JuboContent createJuboContentEntity() {
        JuboContent juboContent = new JuboContent("주일 1부예배", RandomString.make(2000));
        return juboContentRepository.save(juboContent);
    }

    @Test
    void create() {
        JuboContent juboContent = createJuboContentEntity();

        assertThat(juboContent.getId()).isNotNull();
        assertThat(juboContent.getTitle()).isEqualTo("주일 1부예배");
        assertThat(juboContent.getContent().length()).isEqualTo(2000);
    }

    @Test
    void read() {
        JuboContent juboContent = createJuboContentEntity();

        Optional<JuboContent> optionalContent = juboContentRepository.findById(juboContent.getId());
        assertThat(optionalContent.isPresent()).isTrue();
    }

    @Test
    void update() {
        JuboContent juboContent = createJuboContentEntity();
        juboContent.updateTitle("주일 2부예배");

        String originContent = juboContent.getContent();
        juboContent.updateContent(RandomString.make(2000));

        JuboContent updatedJuboContent = juboContentRepository.save(juboContent);

        assertThat(updatedJuboContent.getTitle()).isEqualTo("주일 2부예배");
        assertThat(updatedJuboContent.getContent()).isNotEqualTo(originContent);
    }

    @Test
    void delete() {
        JuboContent juboContent = createJuboContentEntity();
        juboContentRepository.delete(juboContent);

        Optional<JuboContent> optionalContent = juboContentRepository.findById(juboContent.getId());
        assertThat(optionalContent.isPresent()).isFalse();
    }
}