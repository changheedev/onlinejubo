package com.github.changhee_choi.jubo.manager.web.payload;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * @author Changhee Choi
 * @since 06/07/2020
 */
@Data
@NoArgsConstructor
public class JuboRegistrationPayload {
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;

    @NotNull
    private LocalDateTime startDate;

    @Builder
    public JuboRegistrationPayload(@NotBlank @Size(min = 1, max = 50) String title, @NotNull LocalDateTime startDate) {
        this.title = title;
        this.startDate = startDate;
    }
}
