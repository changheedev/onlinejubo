package com.github.changhee_choi.jubo.core.domain.jubo;

import com.github.changhee_choi.jubo.core.domain.user.User;
import com.github.changhee_choi.jubo.core.domain.user.UserRepository;
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
class ChurchRepositoryTests {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ChurchRepository churchRepository;

    @Autowired
    private UserRepository userRepository;

    private Church createChurchEntity() {
        User user = User.builder().name("test_user").email("test@email.com").password("password").build();
        userRepository.save(user);
        Church church = Church.builder().name("test_church").memberNum(30).user(user).build();
        return churchRepository.save(church);
    }

    @Test
    void create() {
        Church church = createChurchEntity();

        assertThat(church.getId()).isNotNull();
        assertThat(church.getId().toString().length()).isEqualTo(36);
        logger.info(church.getId().toString());

        User user = church.getUser();
        assertThat(user).isNotNull();
        assertThat(user.getChurch()).isNotNull();

        assertThat(church.getName()).isEqualTo("test_church");
        assertThat(church.getMemberNum()).isEqualTo(30);
        assertThat(church.getCreatedBy()).isEqualTo("testUser(test@email.com)");
        assertThat(church.getLastModifiedBy()).isEqualTo("testUser(test@email.com)");
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