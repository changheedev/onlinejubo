package com.github.changhee_choi.jubo.manager.config;

import com.github.changhee_choi.jubo.manager.web.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Changhee Choi
 * @since 01/07/2020
 */
@Configuration
public class JwtConfig {

    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.expirationSeconds}")
    private String expirationSeconds;

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil(secretKey, Long.parseLong(expirationSeconds));
    }
}
