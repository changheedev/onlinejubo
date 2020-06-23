package com.github.changhee_choi.jubo.core.domain.user;

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
class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    private User createUserEntity() {
        User user = User.builder().name("test_user").email("test@email.com").password("password").build();
        return userRepository.save(user);
    }

    @Test
    void create() {
        User user = createUserEntity();

        assertThat(user.getId()).isNotNull();
        assertThat(user.getName()).isEqualTo("test_user");
        assertThat(user.getEmail()).isEqualTo("test@email.com");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.isAccountLocked()).isFalse();
        assertThat(user.isEmailConfirmed()).isFalse();
        assertThat(user.isWithdraw()).isFalse();
        assertThat(user.getCreatedBy()).isEqualTo("testUser(test@email.com)");
        assertThat(user.getLastModifiedBy()).isEqualTo("testUser(test@email.com)");
        assertThat(user.getCreatedDate()).isNotNull();
        assertThat(user.getLastModifiedDate()).isNotNull();
    }

    @Test
    void createWhenDuplicatedEmailThatThrowDataIntegrityViolationException() {
        assertThatThrownBy(() -> {
            createUserEntity();
            createUserEntity();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void read() {
        User user = createUserEntity();
        Optional<User> optionalReadUser = userRepository.findById(user.getId());
        assertThat(optionalReadUser.isPresent()).isTrue();

        User readUser = optionalReadUser.get();
        assertThat(readUser.getRoles().size()).isEqualTo(1);
    }

    @Test
    void update() {
        User user = createUserEntity();

        user.updateEmail("updated@email.com");
        user.updateName("updated_user");
        user.updatePassword("updatedPassword");
        user.lockAccount();
        user.withdrawAccount();
        user.confirmAccountEmail();

        User updatedUser = userRepository.save(user);
        userRepository.flush();

        assertThat(updatedUser.getEmail()).isEqualTo("updated@email.com");
        assertThat(updatedUser.getName()).isEqualTo("updated_user");
        assertThat(updatedUser.getPassword()).isEqualTo("updatedPassword");
        assertThat(updatedUser.isAccountLocked()).isTrue();
        assertThat(updatedUser.isWithdraw()).isTrue();
        assertThat(updatedUser.isEmailConfirmed()).isTrue();
        assertThat(updatedUser.getLastModifiedDate()).isNotEqualTo(updatedUser.getCreatedDate());
    }

    @Test
    void delete() {
        User user = createUserEntity();
        userRepository.delete(user);

        Optional<User> deletedUser = userRepository.findById(user.getId());
        assertThat(deletedUser.isPresent()).isFalse();
    }
}