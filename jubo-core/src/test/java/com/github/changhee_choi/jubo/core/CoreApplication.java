package com.github.changhee_choi.jubo.core;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author Changhee Choi
 * @since 20/06/2020
 */
@EnableJpaAuditing
@SpringBootApplication
@Import(CoreTestConfig.class)
public class CoreApplication {
}
