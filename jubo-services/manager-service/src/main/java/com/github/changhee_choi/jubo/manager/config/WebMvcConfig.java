package com.github.changhee_choi.jubo.manager.config;

import com.github.changhee_choi.jubo.manager.web.interceptor.MappedApiLoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Changhee Choi
 * @since 23/07/2020
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MappedApiLoggingInterceptor())
                .addPathPatterns("/**");
    }
}
