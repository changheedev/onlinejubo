package com.github.changhee_choi.jubo.core.domain.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Changhee Choi
 * @since 23/06/2020
 */
@DataJpaTest
class LoginLogRepositoryTests {

    @Autowired
    private LoginLogRepository loginLogRepository;

    @Test
    public void create() {
        LoginLog loginLog = LoginLog.builder().loginIp("111.222.333.444").loginSuccess(true).build();
        loginLogRepository.save(loginLog);
        assertThat(loginLog.getLoginDate()).isNotNull();
    }
}