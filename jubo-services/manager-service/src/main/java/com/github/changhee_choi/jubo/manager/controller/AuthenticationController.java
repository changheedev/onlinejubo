package com.github.changhee_choi.jubo.manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.changhee_choi.jubo.core.userdetails.ChurchManagerDetails;
import com.github.changhee_choi.jubo.core.util.JwtUtil;
import com.github.changhee_choi.jubo.manager.model.web.AuthenticationRequest;
import com.github.changhee_choi.jubo.manager.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Changhee Choi
 * @since 01/07/2020
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final AuthenticationService authenticationService;

    @PostMapping("/authorize")
    public ResponseEntity authorize(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) {

        ChurchManagerDetails userDetails = authenticationService.authenticate(authenticationRequest);
        String token = jwtUtil.generateToken(objectMapper.convertValue(userDetails, Map.class));
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
