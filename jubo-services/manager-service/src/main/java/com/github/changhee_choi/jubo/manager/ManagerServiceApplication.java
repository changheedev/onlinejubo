package com.github.changhee_choi.jubo.manager;

import com.github.changhee_choi.jubo.core.domain.DomainConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @author Changhee Choi
 * @since 24/06/2020
 */
@SpringBootApplication(scanBasePackages = {"com.github.changhee_choi.jubo"})
@Import(DomainConfig.class)
public class ManagerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagerServiceApplication.class, args);
    }
}
