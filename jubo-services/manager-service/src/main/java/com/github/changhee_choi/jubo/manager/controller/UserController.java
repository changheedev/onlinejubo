package com.github.changhee_choi.jubo.manager.controller;

import com.github.changhee_choi.jubo.manager.model.web.SignUpRequest;
import com.github.changhee_choi.jubo.manager.service.DuplicateEmailException;
import com.github.changhee_choi.jubo.manager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;

/**
 * @author Changhee Choi
 * @since 24/06/2020
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("")
    public ResponseEntity<UserDetails> signUp(@Valid @RequestBody SignUpRequest request, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ValidationException("회원가입 요청 데이터가 올바르지 않습니다.");
        }

        UserDetails userDetails = userService.signUp(request);
        return ResponseEntity.ok(userDetails);
    }

    @ExceptionHandler(value = {ValidationException.class})
    public ResponseEntity validationExceptionHandler(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(value = {DuplicateEmailException.class})
    public ResponseEntity duplicateEmailExceptionHandler(Exception e) {
        return ResponseEntity.unprocessableEntity().body(e.getMessage());
    }
}
