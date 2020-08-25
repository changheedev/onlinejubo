package com.github.changhee_choi.jubo.manager;

import com.github.changhee_choi.jubo.manager.web.payload.ChurchManagerTokenClaims;
import com.github.changhee_choi.jubo.manager.web.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author Changhee Choi
 * @since 23/07/2020
 */
public class WebMvcTestSupport extends TestParameterSupport {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    protected WebApplicationContext webApplicationContext;
    protected MockMvc mockMvc;

    protected final String managerRole = "CHURCH_MANAGER";

    protected Cookie createTokenCookie(UUID churchId, String role) {
        return new Cookie("ACCESS_TOKEN", createAccessToken(churchId, "ROLE_" + role));
    }

    private String createAccessToken(UUID churchId, String role) {
        ChurchManagerTokenClaims claims = ChurchManagerTokenClaims.builder()
                .id(1L)
                .name("TestUser")
                .roles(Arrays.asList(role))
                .churchId(churchId)
                .build();

        return jwtUtil.generateToken(claims);
    }
}
