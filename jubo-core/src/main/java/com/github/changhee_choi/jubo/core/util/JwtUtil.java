package com.github.changhee_choi.jubo.core.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

/**
 * @author Changhee Choi
 * @since 27/06/2020
 */
public class JwtUtil {
    private final String secretKey;
    private final Long expirationSeconds;

    public JwtUtil(String secretKey, Long expirationSeconds) {
        this.secretKey = secretKey;
        this.expirationSeconds = expirationSeconds;
    }

    public String generateToken(Map<String, Object> claims) {
        LocalDateTime expiryDate = LocalDateTime.now().plusSeconds(expirationSeconds);
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expiryDate.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    public Claims validateToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody();
        } catch (JwtException e) {
            throw new AccessDeniedException(String.format("유효하지 않은 토큰으로 접근이 시도되었습니다 [%s]", token));
        }
    }

    public Long getExpirationSeconds() {
        return expirationSeconds;
    }
}
