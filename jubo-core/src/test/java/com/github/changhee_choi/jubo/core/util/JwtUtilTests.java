package com.github.changhee_choi.jubo.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.changhee_choi.jubo.core.domain.user.TestUserDetails;
import com.github.changhee_choi.jubo.core.json.JsonConfig;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

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
        userDetails.setCreatedBy("Server");
        userDetails.setCreatedDate(LocalDateTime.now());

        Collection<? extends GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("USER_ROLE"));
        userDetails.setAuthorities(authorities);

        return userDetails;
    }

    @Test
    void generateToken() {
        TestUserDetails userDetails = createTestUserDetails();
        Map<String, Object> claims = mapper.convertValue(userDetails, Map.class);
        assertThat(claims.get("createdDate").toString().matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));

        String token = jwtUtil.generateToken(claims);
        System.out.println(token);
        assertThat(token.startsWith("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9")).isTrue();
    }

    @Test
    void validateToken() {
        TestUserDetails userDetails = createTestUserDetails();
        Map<String, Object> claimsMap = mapper.convertValue(userDetails, Map.class);
        String token = jwtUtil.generateToken(claimsMap);

        Claims claims = jwtUtil.validateToken(token);
        assertThat(claims.get("id", Long.class)).isEqualTo(1L);
        assertThat(claims.get("name", String.class)).isEqualTo("TestUser");
        assertThat(claims.get("createdDate", String.class).matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }

    @Test
    void validationToken에서_토큰이_만료되었다면_AccessDeniedException이_던져진다() throws Exception{
        TestUserDetails userDetails = createTestUserDetails();
        Map<String, Object> claimsMap = mapper.convertValue(userDetails, Map.class);
        String token = jwtUtil.generateToken(claimsMap);

        Thread.sleep(2010L);
        assertThatThrownBy(() -> jwtUtil.validateToken(token))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void validationToken에서_토큰의_Key가_다르면_AccessDeniedException이_던져진다(){
        TestUserDetails userDetails = createTestUserDetails();
        Map<String, Object> claimsMap = mapper.convertValue(userDetails, Map.class);

        JwtUtil otherJwtUtil = new JwtUtil("secret_key2", 10L);
        String token = otherJwtUtil.generateToken(claimsMap);

        assertThatThrownBy(() -> jwtUtil.validateToken(token))
                .isInstanceOf(AccessDeniedException.class);
    }
}