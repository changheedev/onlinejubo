package com.github.changhee_choi.jubo.manager.service;

import javax.validation.ValidationException;

/**
 * @author Changhee Choi
 * @since 24/06/2020
 */
public class DuplicateEmailException extends ValidationException {
    public DuplicateEmailException(String s) {
    }
}
