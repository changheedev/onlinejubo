package com.github.changhee_choi.jubo.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.changhee_choi.jubo.core.domain.user.TestUserDetails;
import com.github.changhee_choi.jubo.core.domain.user.UserTokenClaims;
import com.github.changhee_choi.jubo.core.config.JsonConfig;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Changhee Choi
 * @since 27/06/2020
 */
class JwtUtilTests {
    private JwtUtil jwtUtil;
    private ObjectMapper mapper;

    @BeforeEach
    void setup() {
        jwtUtil = new JwtUtil("secret_key", 2L);

        mapper = new ObjectMapper();
        mapper.registerModule(new JsonConfig().jsonMapperJava8DateTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private TestUserDetails createTestUserDetails() {
        TestUserDetails userDetails = new TestUserDetails();
        userDetails.setId(1L);
        userDetails.setName("TestUser");
        userDetails.setEmail("test@email.com");
        userDetails.setCreatedBy(1L);
        userDetails.setCreatedDate(LocalDateTime.now());

        Collection<? extends GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("USER_ROLE"));
        userDetails.setAuthorities(authorities);

        return userDetails;
    }

    @Test
    void generateToken() {
        UserTokenClaims claims = UserTokenClaims.builder().id(1L).name("TestUser").roles(Arrays.asList("ROLE_USER")).build();

        String token = jwtUtil.generateToken(claims);
        System.out.println(token);
        assertThat(token.startsWith("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9")).isTrue();
    }

    @Test
    void validateToken() {

        UserTokenClaims claims = UserTokenClaims.builder().id(1L).name("TestUser").roles(Arrays.asList("ROLE_USER")).build();
        String token = jwtUtil.generateToken(claims);

        Claims validatedClaims = jwtUtil.validateToken(token);
        assertThat(validatedClaims.get("id", Long.class)).isEqualTo(1L);
        assertThat(validatedClaims.get("name", String.class)).isEqualTo("TestUser");
    }

    @Test
    void validationToken에서_토큰이_만료되었다면_AccessDeniedException이_던져진다() throws Exception{
        UserTokenClaims claims = UserTokenClaims.builder().id(1L).name("TestUser").roles(Arrays.asList("ROLE_USER")).build();
        String token = jwtUtil.generateToken(claims);

        Thread.sleep(2010L);
        assertThatThrownBy(() -> jwtUtil.validateToken(token))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void validationToken에서_토큰의_Key가_다르면_AccessDeniedException이_던져진다(){
        JwtUtil otherJwtUtil = new JwtUtil("secret_key2", 10L);
        UserTokenClaims claims = UserTokenClaims.builder().id(1L).name("TestUser").roles(Arrays.asList("ROLE_USER")).build();
        String token = otherJwtUtil.generateToken(claims);

        assertThatThrownBy(() -> jwtUtil.validateToken(token))
                .isInstanceOf(AccessDeniedException.class);
    }
}