package com.github.changhee_choi.jubo.core.domain.jubo;

import com.github.changhee_choi.jubo.core.domain.user.User;
import com.github.changhee_choi.jubo.core.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Changhee Choi
 * @since 21/06/2020
 */
@DataJpaTest
class JuboRepositoryTests {

    @Autowired
    private JuboRepository juboRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChurchRepository churchRepository;

    private Jubo createJuboEntity() {
        User user = User.builder().name("test_user").email("test@email.com").password("password").build();
        userRepository.save(user);
        Church church = Church.builder().name("test_church").memberNum(30).build();
        churchRepository.save(church);

        Jubo jubo = Jubo.builder()
                .title("2020-06-21일 주보")
                .startDate(LocalDateTime.of(2020, 6, 21, 0, 0))
                .church(church).build();

        return juboRepository.save(jubo);
    }

    @Test
    void create() {
        Jubo jubo = createJuboEntity();

        assertThat(jubo.getId()).isNotNull();
        assertThat(jubo.getTitle()).isEqualTo("2020-06-21일 주보");
        assertThat(jubo.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .isEqualTo("2020-06-21 00:00:00");
        assertThat(jubo.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .isEqualTo("2020-06-27 00:00:00");
        assertThat(jubo.getViewCount()).isEqualTo(0);
        assertThat(jubo.getCreatedBy()).isEqualTo("testUser(test@email.com)");
        assertThat(jubo.getLastModifiedBy()).isEqualTo("testUser(test@email.com)");
        assertThat(jubo.getCreatedDate()).isNotNull();
        assertThat(jubo.getLastModifiedDate()).isNotNull();

        assertThat(jubo.getChurch().getJuboList().contains(jubo)).isTrue();
    }

    @Test
    void read() {
        Jubo jubo = createJuboEntity();
        Optional<Jubo> optionalJubo = juboRepository.findById(jubo.getId());
        assertThat(optionalJubo.isPresent()).isTrue();
    }

    @Test
    void update() {
        Jubo jubo = createJuboEntity();

        jubo.updateTitle("2020-06-28일 주보");
        jubo.updateStartDate(LocalDateTime.of(2020, 6, 28, 0, 0));

        Jubo updatedJubo = juboRepository.save(jubo);
        juboRepository.flush();

        assertThat(updatedJubo.getTitle()).isEqualTo("2020-06-28일 주보");
        assertThat(updatedJubo.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .isEqualTo("2020-06-28 00:00:00");
        assertThat(updatedJubo.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .isEqualTo("2020-07-04 00:00:00");
        assertThat(updatedJubo.getLastModifiedDate()).isNotEqualTo(jubo.getCreatedDate());
    }

    @Test
    void delete() {
        Jubo jubo = createJuboEntity();
        juboRepository.delete(jubo);

        Optional<Jubo> optionalJubo = juboRepository.findById(jubo.getId());
        assertThat(optionalJubo.isPresent()).isFalse();
    }
}