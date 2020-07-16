package com.github.changhee_choi.jubo.manager.web.filter;

import com.github.changhee_choi.jubo.core.domain.user.UserTokenClaims;
import com.github.changhee_choi.jubo.core.util.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Changhee Choi
 * @since 02/07/2020
 */
class JwtAuthenticationFilterTests {
    private JwtUtil jwtUtil = new JwtUtil("test-secret", 10L);
    private JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtil);

    @AfterEach
    public void teardown() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Test
    public void Request에_액세스토큰_쿠키가_존재하는_경우_SecurityContext에_Authentication_정보가_생성된다()
            throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        UserTokenClaims claims = UserTokenClaims.builder().id(1L).name("TestUser").roles(Arrays.asList("ROLE_USER")).build();
        String token = jwtUtil.generateToken(claims);
        request.setCookies(new Cookie("ACCESS_TOKEN", token));

        jwtAuthenticationFilter.doFilter(request, response, filterChain);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        assertThat(securityContext.getAuthentication()).isNotNull();

        UserTokenClaims userTokenClaims = (UserTokenClaims)authentication.getPrincipal();
        assertThat(userTokenClaims.getId()).isEqualTo(1L);
        assertThat(userTokenClaims.getName()).isEqualTo("TestUser");
        assertThat(userTokenClaims.getRoles().contains("ROLE_USER")).isTrue();
    }

    @Test
    public void Request에_액세스토큰_쿠키가_존재하지_않는_경우_SecurityContext에_Authentication_정보가_생성되지_않는다()
            throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        jwtAuthenticationFilter.doFilter(request, response, filterChain);
        SecurityContext securityContext = SecurityContextHolder.getContext();

        assertThat(securityContext.getAuthentication()).isNull();
    }
}