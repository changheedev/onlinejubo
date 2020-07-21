package com.github.changhee_choi.jubo.core.domain.jubo;

import com.github.changhee_choi.jubo.core.domain.attachment.Attachment;
import com.github.changhee_choi.jubo.core.domain.attachment.AttachmentDetails;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Changhee Choi
 * @since 14/07/2020
 */
@Data
public class JuboContentDetails {
    private Long id;
    private String title;
    private String content;
    private List<AttachmentDetails> attachments;

    @Builder(access = AccessLevel.PRIVATE)
    private JuboContentDetails(Long id, String title, String content, List<AttachmentDetails> attachments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.attachments = attachments;
    }

    public static JuboContentDetails of(JuboContent entity){
        return JuboContentDetails.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .attachments(entity.getAttachments().stream().map(AttachmentDetails::of).collect(Collectors.toList()))
                .build();
    }
}
