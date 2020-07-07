package com.github.changhee_choi.jubo.core.domain.church;

/**
 * @author Changhee Choi
 * @since 07/07/2020
 */
public class ChurchNotFoundException extends RuntimeException{

    public ChurchNotFoundException(String message) {
        super(message);
    }
}
