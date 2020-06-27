package com.github.changhee_choi.jubo.core.domain.user;

import com.github.changhee_choi.jubo.core.domain.jubo.Church;
import com.github.changhee_choi.jubo.core.domain.jubo.ChurchRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Changhee Choi
 * @since 20/06/2020
 */
@DataJpaTest
class ChurchManagerTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChurchRepository churchRepository;

    private User createChurchManagerEntity() {
        Church church = Church.builder().name("My Church").memberNum(20).build();
        churchRepository.save(church);

        User user = ChurchManager.builder().name("test_user").email("test@email.com")
                .password("password").church(church).build();
        userRepository.save(user);
        return user;
    }

    @Test
    void create() {
        ChurchManager manager = (ChurchManager)createChurchManagerEntity();

        assertThat(manager.getId()).isNotNull();
        assertThat(manager.getName()).isEqualTo("test_user");
        assertThat(manager.getEmail()).isEqualTo("test@email.com");
        assertThat(manager.getPassword()).isEqualTo("password");
        assertThat(manager.isAccountLocked()).isFalse();
        assertThat(manager.isEmailConfirmed()).isFalse();
        assertThat(manager.isWithdraw()).isFalse();
        assertThat(manager.isServiceAllowed()).isFalse();
        assertThat(manager.getCreatedBy()).isEqualTo("testUser(test@email.com)");
        assertThat(manager.getLastModifiedBy()).isEqualTo("testUser(test@email.com)");
        assertThat(manager.getCreatedDate()).isNotNull();
        assertThat(manager.getLastModifiedDate()).isNotNull();
    }

    @Test
    void createWhenDuplicatedEmailThatThrowDataIntegrityViolationException() {
        assertThatThrownBy(() -> {
            createChurchManagerEntity();
            createChurchManagerEntity();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void read() {
        User user = createChurchManagerEntity();
        Optional<User> optionalReadUser = userRepository.findById(user.getId());
        assertThat(optionalReadUser.isPresent()).isTrue();
    }

    @Test
    void update() {
        ChurchManager manager = (ChurchManager)createChurchManagerEntity();

        manager.updateEmail("updated@email.com");
        manager.updateName("updated_user");
        manager.updatePassword("updatedPassword");
        manager.lockAccount();
        manager.withdrawAccount();
        manager.confirmAccountEmail();
        manager.allowService();

        ChurchManager updatedManager = userRepository.save(manager);
        userRepository.flush();

        assertThat(updatedManager.getEmail()).isEqualTo("updated@email.com");
        assertThat(updatedManager.getName()).isEqualTo("updated_user");
        assertThat(updatedManager.getPassword()).isEqualTo("updatedPassword");
        assertThat(updatedManager.isAccountLocked()).isTrue();
        assertThat(updatedManager.isWithdraw()).isTrue();
        assertThat(updatedManager.isEmailConfirmed()).isTrue();
        assertThat(updatedManager.isServiceAllowed()).isTrue();
        assertThat(updatedManager.getLastModifiedDate()).isNotEqualTo(updatedManager.getCreatedDate());
    }

    @Test
    void delete() {
        User user = createChurchManagerEntity();
        userRepository.delete(user);

        Optional<User> deletedUser = userRepository.findById(user.getId());
        assertThat(deletedUser.isPresent()).isFalse();
    }
}