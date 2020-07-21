package com.github.changhee_choi.jubo.core.domain.attachment;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * @author Changhee Choi
 * @since 14/07/2020
 */
@Data
public class AttachmentDetails {
    private UUID id;
    private String path;
    private String originName;
    private FileType fileType;

    @Builder
    private AttachmentDetails(UUID id, String path, String originName, FileType fileType) {
        this.id = id;
        this.path = path;
        this.originName = originName;
        this.fileType = fileType;
    }

    public static AttachmentDetails of(Attachment entity) {
        return AttachmentDetails.builder()
                .id(entity.getId())
                .path(entity.getPath())
                .originName(entity.getOriginName())
                .fileType(entity.getFileType())
                .build();
    }
}
