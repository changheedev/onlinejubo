package com.github.changhee_choi.jubo.manager.controller;

import com.github.changhee_choi.jubo.manager.service.JuboService;
import com.github.changhee_choi.jubo.manager.web.payload.JuboFormPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/jubo/**")
public class JuboServiceController {

    private final JuboService juboService;

    @PostMapping("")
    public ResponseEntity register(@Valid @RequestBody JuboFormPayload payload, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationException("주보 등록 요청 데이터가 올바르지 않습니다.");
        }
        juboService.register(payload);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ExceptionHandler(value = {ValidationException.class})
    public ResponseEntity validationExceptionHandler(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
