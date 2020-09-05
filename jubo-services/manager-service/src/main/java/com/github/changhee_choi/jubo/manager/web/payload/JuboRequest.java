package com.github.changhee_choi.jubo.manager.web.payload;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Changhee Choi
 * @since 06/07/2020
 */
@Data
@NoArgsConstructor
public class JuboRequest {
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;

    @NotNull
    private LocalDateTime startDate;

    @NotEmpty
    private List<JuboContentRequest> contents;

    @Builder
    public JuboRequest(@NotBlank @Size(min = 1, max = 50) String title, @NotNull LocalDateTime startDate,
                       @NotEmpty List<JuboContentRequest> contents) {
        this.title = title;
        this.startDate = startDate;
        this.contents = contents;
    }
}
