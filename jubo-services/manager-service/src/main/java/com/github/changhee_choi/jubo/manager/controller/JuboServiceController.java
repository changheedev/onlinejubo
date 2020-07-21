package com.github.changhee_choi.jubo.manager.controller;

import com.github.changhee_choi.jubo.core.domain.jubo.JuboContentDetails;
import com.github.changhee_choi.jubo.core.domain.jubo.JuboDetails;
import com.github.changhee_choi.jubo.manager.service.JuboContentService;
import com.github.changhee_choi.jubo.manager.service.JuboService;
import com.github.changhee_choi.jubo.manager.web.payload.ChurchManagerTokenClaims;
import com.github.changhee_choi.jubo.manager.web.payload.JuboContentRequest;
import com.github.changhee_choi.jubo.manager.web.payload.JuboRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;

/**
 * @author Changhee Choi
 * @since 06/07/2020
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/jubo")
@Slf4j
public class JuboServiceController {

    private final JuboService juboService;
    private final JuboContentService juboContentService;

    @PostMapping("")
    public ResponseEntity registerJubo(@Valid @RequestBody JuboRequest payload,
                                       Authentication authentication, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.debug(bindingResult.getFieldErrors().toString());
            throw new ValidationException("주보 등록 요청 데이터가 올바르지 않습니다.");
        }

        ChurchManagerTokenClaims tokenClaims = (ChurchManagerTokenClaims)authentication.getPrincipal();

        JuboDetails juboDetails = juboService.register(tokenClaims.getChurchId(), payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(juboDetails);
    }

    @PostMapping("/{id}/contents")
    public ResponseEntity registerContent(@PathVariable Long id,
                                          @Valid @RequestBody JuboContentRequest payload,
                                          Authentication authentication, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.debug(bindingResult.getFieldErrors().toString());
            throw new ValidationException("주보 컨텐츠 등록 요청 데이터가 올바르지 않습니다.");
        }

        ChurchManagerTokenClaims tokenClaims = (ChurchManagerTokenClaims)authentication.getPrincipal();

        JuboContentDetails contentDetails = juboContentService.register(tokenClaims.getChurchId(), id, payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(contentDetails);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateJubo(@PathVariable Long id, @Valid @RequestBody JuboRequest payload,
                                     Authentication authentication, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            log.debug(bindingResult.getFieldErrors().toString());
            throw new ValidationException("주보 업데이트 요청 데이터가 올바르지 않습니다.");
        }

        ChurchManagerTokenClaims tokenClaims = (ChurchManagerTokenClaims)authentication.getPrincipal();

        JuboDetails juboDetails = juboService.update(tokenClaims.getChurchId(), id, payload);
        return ResponseEntity.status(HttpStatus.OK).body(juboDetails);
    }
}
