package com.github.changhee_choi.jubo.core.domain.user;

import com.github.changhee_choi.jubo.core.domain.user.Role;
import com.github.changhee_choi.jubo.core.domain.user.RoleRepository;
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
class RoleRepositoryTests {

    @Autowired
    private RoleRepository roleRepository;

    private Role createRoleEntity() {
        Role role = new Role("role_user");
        return roleRepository.save(role);
    }

    @Test
    void create() {
        Role role = createRoleEntity();

        assertThat(role.getId()).isNotNull();
        assertThat(role.getName()).isEqualTo("ROLE_USER");
        assertThat(role.getCreatedBy()).isEqualTo("testUser(test@email.com)");
        assertThat(role.getLastModifiedBy()).isEqualTo("testUser(test@email.com)");
        assertThat(role.getCreatedDate()).isNotNull();
        assertThat(role.getLastModifiedDate()).isNotNull();
    }

    @Test
    void createWhenDuplicatedRoleNameThatThrowDataIntegrityViolationException() {
        assertThatThrownBy(() -> {
            createRoleEntity();
            createRoleEntity();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void readEntityById() {
        Role role = createRoleEntity();

        Optional<Role> readRole = roleRepository.findById(role.getId());
        assertThat(readRole.isPresent()).isTrue();
    }

    @Test
    void readEntityByName() {
        Role role = createRoleEntity();

        Optional<Role> readRole = roleRepository.findByName(role.getName());
        assertThat(readRole.isPresent()).isTrue();
    }

    @Test
    void delete() {
        Role role = createRoleEntity();
        roleRepository.delete(role);

        Optional<Role> deletedRole = roleRepository.findById(role.getId());
        assertThat(deletedRole.isPresent()).isFalse();
    }
}