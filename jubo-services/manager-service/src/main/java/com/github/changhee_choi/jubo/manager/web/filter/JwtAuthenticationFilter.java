package com.github.changhee_choi.jubo.manager.web.filter;

import com.github.changhee_choi.jubo.core.domain.user.UserTokenClaims;
import com.github.changhee_choi.jubo.core.util.CookieUtil;
import com.github.changhee_choi.jubo.core.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Changhee Choi
 * @since 02/07/2020
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Optional<Cookie> jwtCookie = CookieUtil.getCookie(request, "ACCESS_TOKEN");

        if (jwtCookie.isPresent()) {
            Claims claims = jwtUtil.validateToken(jwtCookie.get().getValue());
            UserTokenClaims userTokenClaims = UserTokenClaims.builder()
                    .id(claims.get("id", Long.class))
                    .name(claims.get("name", String.class))
                    .roles(claims.get("roles", List.class)).build();

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            userTokenClaims,
                            null,
                            userTokenClaims.getRoles()
                                    .stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .collect(Collectors.toSet()));

            //인증 정보를 SecurityContextHolder 에 저장
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        filterChain.doFilter(request, response);
    }
}
