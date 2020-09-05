package com.github.changhee_choi.jubo.manager.web.payload;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Changhee Choi
 * @since 06/07/2020
 */
@Data
@NoArgsConstructor
public class JuboContentRequest {
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;

    @NotBlank
    private String content;

    private List<UUID> attachmentIds = new ArrayList<>();

    @Builder
    public JuboContentRequest(@NotBlank @Size(min = 1, max = 50) String title, @NotBlank String content,
                              List<UUID> attachmentIds) {
        this.title = title;
        this.content = content;
        if (attachmentIds != null) {
            this.attachmentIds.addAll(attachmentIds);
        }
    }
}