package com.github.changhee_choi.jubo.manager.controller;

import com.github.changhee_choi.jubo.manager.service.AuthenticationService;
import com.github.changhee_choi.jubo.manager.userdetails.ChurchManagerDetails;
import com.github.changhee_choi.jubo.manager.web.payload.AuthenticationRequest;
import com.github.changhee_choi.jubo.manager.web.payload.ChurchManagerTokenClaims;
import com.github.changhee_choi.jubo.manager.web.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

/**
 * @author Changhee Choi
 * @since 01/07/2020
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final JwtUtil jwtUtil;
    private final AuthenticationService authenticationService;

    @PostMapping("/authorize")
    public ResponseEntity authorize(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) {

        ChurchManagerDetails userDetails = authenticationService.authenticate(authenticationRequest);
        String token = jwtUtil.generateToken(ChurchManagerTokenClaims.builder()
                .id(userDetails.getId())
                .name(userDetails.getName())
                .roles(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .churchId(userDetails.getChurchInfo().getId())
                .build());
        Cookie cookie = new Cookie("ACCESS_TOKEN", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(jwtUtil.getExpirationSeconds().intValue());
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity authenticationExceptionHandler(Exception e) {
        log.debug("로그인 실패 - {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
