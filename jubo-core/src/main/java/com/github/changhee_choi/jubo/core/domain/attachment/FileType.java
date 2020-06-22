package com.github.changhee_choi.jubo.core.domain.attachment;

/**
 * @author Changhee Choi
 * @since 22/06/2020
 */
public enum FileType {
    IMAGE,
    UNSUPPORTED;

    public static FileType getFileType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        if (extension.equals("jpg") || extension.equals("png") || extension.equals("jpeg") || extension.equals("gif")) {
            return IMAGE;
        }

        return UNSUPPORTED;
    }
}
