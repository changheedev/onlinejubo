package com.github.changhee_choi.jubo.core.domain.attachment;

/**
 * @author Changhee Choi
 * @since 15/07/2020
 */
public class AttachmentNotFoundException extends RuntimeException{
    public AttachmentNotFoundException(String message) {
        super(message);
    }
}
